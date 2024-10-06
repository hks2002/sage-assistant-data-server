/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:57:07                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 01:36:23                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.da.sageassistantserver.dao.AttachmentMapper;
import com.da.sageassistantserver.dao.DocsMapper;
import com.da.sageassistantserver.model.Attachment;
import com.da.sageassistantserver.model.Docs;
import com.da.sageassistantserver.utils.ResponseJsonHelper;
import com.da.sageassistantserver.utils.ResponseJsonHelper.MsgTyp;
import com.da.sageassistantserver.utils.Utils;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

/**
 * DocsService is provide the /docs/* api, documents is stored at the ZHU HAI
 * database/file server
 */
@Slf4j
@Service
public class DocsService {

    @Value("${attachment.path.linux}")
    private String linuxPath;

    @Value("${attachment.path.windows}")
    private String windowsPath;

    @Value("${attachment.path.folder.deep}")
    private int subFolderDeep;

    @Value("${attachment.path.folder.len}")
    private int subFolderLen;

    @Autowired
    private DocsMapper docsMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private LogService logService;

    public Integer createDocs(Docs docs) {
        docs.setCreate_at(new Timestamp(System.currentTimeMillis()));
        docs.setUpdate_at(new Timestamp(System.currentTimeMillis()));
        docs.setCreate_by(0L);
        docs.setUpdate_by(0L);
        return docsMapper.insert(docs);
    }

    public Integer createDocs(
        String fileName,
        String location,
        Long size,
        Timestamp createAt,
        Timestamp lastModifiedAt,
        String md5
    ) {
        Docs docs = new Docs();
        docs.setFile_name(fileName);
        docs.setLocation(location);
        docs.setSize(size);
        docs.setDoc_create_at(createAt);
        docs.setDoc_modified_at(lastModifiedAt);
        docs.setMd5(md5);
        return createDocs(docs);
    }

    public Docs getDocsById(Long id) {
        return docsMapper.selectById(id);
    }

    public List<Docs> getDocsByPN(String Pn, String auth, Boolean notUsingLike) {
        LambdaQueryWrapper<Docs> queryWrapper = new LambdaQueryWrapper<>();
        if (notUsingLike == true) {
            queryWrapper.eq(Docs::getFile_name, Pn);
        } else {
            queryWrapper.like(Docs::getFile_name, Utils.makeShortPn(Pn));
        }
        queryWrapper.orderBy(true, false, Docs::getDoc_modified_at);
        queryWrapper.last("limit 20");

        List<Docs> docs = docsMapper.selectList(queryWrapper);

        String docBasePath = servletContext.getRealPath("/");
        String attachmentPath = Utils.isWin() ? windowsPath : linuxPath;

        // background search document in Sage Dms server , length >= 5 means more
        // precise target
        CompletableFuture.runAsync(() -> {
            if (Pn.length() >= 5) {
                log.debug("Background searching document in Sage Dms server");
                List<Docs> docsInDms = DmsService.getDocuments(auth, Pn);
                List<Docs> docsNew = new ArrayList<>();

                // if docsInDms's doc not in docs, add it,
                for (Docs docInDms : docsInDms) {
                    boolean found = false;
                    for (Docs doc : docs) {
                        // 581 is an error file, need down it again.
                        if (docInDms.getFile_name().equals(doc.getFile_name()) && doc.getSize() != 581) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        docsNew.add(docInDms);
                    }
                }

                // download, update info, moving
                DmsService.downloadDmsDocs(docsNew, docBasePath);
                updateDocInfo(new File(docBasePath));
                for (Docs newDoc : docsNew) {
                    Utils.moveFile(
                        Paths.get(docBasePath + "/" + newDoc.getFile_name()).toFile(),
                        Paths.get(attachmentPath),
                        subFolderDeep,
                        subFolderLen
                    );
                }
            }
        });

        return docs;
    }

    public List<Docs> getDocsByMd5(String md5) {
        LambdaQueryWrapper<Docs> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Docs::getMd5, md5);
        return docsMapper.selectList(queryWrapper);
    }

    public int updateDocsByWrapper(Docs docs, LambdaUpdateWrapper<Docs> wrapper) {
        docs.setUpdate_at(new Timestamp(System.currentTimeMillis()));
        docs.setUpdate_by(0L);
        return docsMapper.update(docs, wrapper);
    }

    public int updateDocsByFileName(
        String fileName,
        String location,
        Long size,
        Timestamp createAt,
        Timestamp lastModifiedAt,
        String md5
    ) {
        Docs docs = new Docs();
        docs.setFile_name(fileName);
        docs.setLocation(location);
        docs.setSize(size);
        docs.setDoc_create_at(createAt);
        docs.setDoc_modified_at(lastModifiedAt);
        docs.setMd5(md5);
        LambdaUpdateWrapper<Docs> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Docs::getFile_name, fileName);
        return updateDocsByWrapper(docs, updateWrapper);
    }

    public int deleteDocsByFileName(String fileName) {
        LambdaUpdateWrapper<Docs> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Docs::getFile_name, fileName);
        return docsMapper.delete(updateWrapper);
    }

    public int deleteDocsByMd5(String md5) {
        LambdaUpdateWrapper<Docs> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Docs::getMd5, md5);
        return docsMapper.delete(updateWrapper);
    }

    public int deleteDocsById(Long id) {
        return docsMapper.deleteById(id);
    }

    /**
     * Updates the information of a file or all files in a directory.
     *
     * @param file the file or directory whose information to be updated
     *
     * @Note this method update database
     */
    public void updateDocInfo(File file) {
        if (file.isFile()) {
            log.info("[Update] doc info: {}", file.getAbsolutePath());
            String fileName = file.getName();
            int dotIndex = fileName.lastIndexOf(".");
            String fileNameNoExt = dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
            // skip hidden files
            if (fileName.startsWith("~") || fileName.startsWith("$") || fileName.toLowerCase().equals("thumbs.db")) {
                file.delete();
                return;
            }

            Docs docs = new Docs();
            docs.setFile_name(fileName);

            BasicFileAttributes attribute = null;
            try {
                attribute = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                if (attribute != null) {
                    docs.setDoc_create_at(new Timestamp(attribute.creationTime().toMillis()));
                    docs.setDoc_modified_at(new Timestamp(attribute.lastModifiedTime().toMillis()));
                    docs.setSize(attribute.size());
                }
                docs.setLocation(Utils.getPathByFileName(fileNameNoExt, subFolderDeep, subFolderLen));
                docs.setMd5(Utils.computerMd5(file));

                if (getDocsByPN(fileName, null, true).size() == 0) {
                    logService.addLog("DOC_INFO_CREATE", "system", "Docs Service", fileName);
                    createDocs(docs);
                } else {
                    logService.addLog("DOC_INFO_UPDATE", "system", "Docs Service", fileName);
                    LambdaUpdateWrapper<Docs> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.eq(Docs::getFile_name, docs.getFile_name());
                    updateDocsByWrapper(docs, updateWrapper);
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        if (file.isDirectory()) {
            // skip META-INF and WEB-INF folder
            if (file.getName().equals("META-INF") || file.getName().equals("WEB-INF")) {
                return;
            }

            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    updateDocInfo(f);
                }
            }
        }
    }

    /**
     * Delete doc info if document is not exist in server.
     * <p>
     * Update doc location if document is exist in server.
     * This function never delete the document in server.
     *
     * @throws None
     */
    public void cleanDocInfos(File directory) {
        // Check if the directory is the docs location
        LambdaQueryWrapper<Docs> queryWrapper = new LambdaQueryWrapper<>();
        docsMapper
            .selectList(queryWrapper.last("limit 1"))
            .forEach(docs -> {
                String fileName = docs.getFile_name();
                int dotIndex = fileName.lastIndexOf(".");
                String fileNameNoExt = dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;

                String newLocation = Utils.getPathByFileName(fileNameNoExt, subFolderDeep, subFolderLen);
                File file = new File(directory, newLocation + "/" + fileName);
                if (!file.exists()) {
                    log.info("Clean doc info: {} is not the docs location.", directory.getAbsolutePath());
                    return;
                }
            });

        docsMapper
            .selectList(null)
            .forEach(doc -> {
                String fileName = doc.getFile_name();
                int dotIndex = fileName.lastIndexOf(".");
                String fileNameNoExt = dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;

                String newLocation = Utils.getPathByFileName(fileNameNoExt, subFolderDeep, subFolderLen);
                File file = new File(directory, newLocation + "/" + fileName);
                if (!file.exists()) {
                    log.info("Delete doc info: {}", fileName);
                    logService.addLog("DOC_INFO_DELETE", "system", "Docs Service", fileName);
                    docsMapper.deleteById(doc.getId());
                } else {
                    if (!newLocation.equals(doc.getLocation())) {
                        log.info("Update doc info: {}", fileName);
                        logService.addLog(
                            "DOC_INFO_UPDATE",
                            "system",
                            "Docs Service",
                            fileName,
                            newLocation,
                            doc.getLocation()
                        );

                        doc.setLocation(newLocation);
                        LambdaUpdateWrapper<Docs> updateWrapper = new LambdaUpdateWrapper<>();
                        updateWrapper.eq(Docs::getFile_name, doc.getFile_name());
                        updateDocsByWrapper(doc, updateWrapper);
                    }
                }
            });
    }

    public JSONObject handleFileUpload(final MultipartFile uploadFile, String loginName, String loginUser) {
        try {
            if (uploadFile.isEmpty()) {
                return ResponseJsonHelper.rtnObj(false, MsgTyp.RESULT, "Upload file is empty");
            }
            String docBasePath = servletContext.getRealPath("/");
            String originalFilename = uploadFile.getOriginalFilename();
            File file = new File(docBasePath + "/" + originalFilename);
            Path fullSavePath = Paths.get(docBasePath + "/" + originalFilename);

            log.info("[Upload] [{}] to {} by {}[{}]", originalFilename, fullSavePath, loginName, loginUser);

            Files.createDirectories(fullSavePath.getParent());
            Files.copy(uploadFile.getInputStream(), fullSavePath, StandardCopyOption.REPLACE_EXISTING);

            if (file.exists() && file.length() == uploadFile.getSize()) {
                updateDocInfo(file);
                Utils.moveFile(
                    fullSavePath.toFile(),
                    Paths.get(Utils.isWin() ? windowsPath : linuxPath),
                    subFolderDeep,
                    subFolderLen
                );
                logService.addLog("DOC_UPLOAD_SUCCESS", loginName, loginUser, originalFilename);
                return ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, originalFilename);
            } else {
                log.error(
                    "[Upload] [{}] to {} by {}[{}] with failed!",
                    originalFilename,
                    fullSavePath,
                    loginName,
                    loginUser
                );
                return ResponseJsonHelper.rtnObj(false, MsgTyp.RESULT, originalFilename);
            }
        } catch (Exception e) {
            log.error("[Upload] " + e.getMessage());
            return ResponseJsonHelper.rtnObj(false, MsgTyp.ERROR, e.getMessage());
        }
    }

    public JSONObject handleFileDelete(String path, String loginName, String loginUser) {
        String attachmentPath = Utils.isWin() ? windowsPath : linuxPath;
        String docBasePath = servletContext.getRealPath("/");

        try {
            Path docBaseFilePath = Paths.get(docBasePath + URLDecoder.decode(path, "UTF-8"));
            Path attachmentFilePath = Paths.get(attachmentPath + URLDecoder.decode(path, "UTF-8"));
            if (Files.exists(attachmentFilePath)) {
                Files.delete(attachmentFilePath);
            }
            if (Files.exists(docBaseFilePath)) {
                Files.delete(docBaseFilePath);
            }

            if (Files.exists(attachmentFilePath) || Files.exists(docBaseFilePath)) {
                log.error("[Delete] {} by {}[{}] with failed!", path, loginName, loginUser);
                return ResponseJsonHelper.rtnObj(false, MsgTyp.RESULT, path);
            } else {
                log.info("[Delete] {} by {}[{}] with success!", path, loginName, loginUser);
                logService.addLog("DOC_DELETE_SUCCESS", loginName, loginUser, path);
                return ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, path);
            }
        } catch (Exception e) {
            log.error("[Delete] " + e.getMessage());
            return ResponseJsonHelper.rtnObj(false, MsgTyp.ERROR, e.getMessage());
        }
    }

    public List<Attachment> getAttachmentPath(String pn) {
        if (pn.equals("NULL")) {
            return new ArrayList<>();
        }
        if (pn.endsWith("-")) {
            pn = pn.substring(0, pn.length() - 1);
        }
        if (pn.endsWith("_")) {
            pn = pn.substring(0, pn.length() - 1);
        }
        pn = "%" + pn.toUpperCase() + "%";
        List<Attachment> listAttachment = attachmentMapper.getAttachment(pn);
        for (Attachment o : listAttachment) {
            // make the url with server path
            String pathOri = o.getPath();
            pathOri = pathOri.replace("\\", "/");
            int index = pathOri.lastIndexOf("/");
            if (index > 0) {
                o.setFile(pathOri.substring(index + 1));
            } else {
                o.setFile("ERROR");
            }

            if (pathOri.startsWith("[DOCS_SAGEX3]")) {
                pathOri = "file://192.168.10.47/DOCS_SAGEX3/" + pathOri.substring(14);
            } else {
                // Here keep the path
            }

            o.setPath(pathOri);
        }
        return listAttachment;
    }

    public void cronTask() {
        String docBasePath = servletContext.getRealPath("/");
        String attachmentPath = Utils.isWin() ? windowsPath : linuxPath;
        if (docBasePath.contains("target\\classes")) {
            log.debug("[{}] is target/classes, skip updateDocInfo", docBasePath);
            return;
        }

        log.debug(
            "Start to update doc info from [{}] to [{}], deep {}, len {}",
            docBasePath,
            attachmentPath,
            subFolderDeep,
            subFolderLen
        );
        // update doc info first, this amount of files, would be less
        updateDocInfo(new File(docBasePath));
        Utils.moveFiles(Paths.get(docBasePath), Paths.get(attachmentPath), subFolderDeep, subFolderLen);
    }
}
