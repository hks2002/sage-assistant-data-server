package sageassistant.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sageassistant.service.InvoiceService;
import sageassistant.utils.Utils;

@CrossOrigin
@RestController
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;
	
	@GetMapping("/Data/InvoiceNO")
	public String getInvoiceNO(
			@RequestParam(value = "InvoiceNO", required = false, defaultValue = "%%") String InvoiceNO,
			@RequestParam(value = "Count", required = false, defaultValue = "50") Integer count) {
		return  Utils.stringRemovePageInfo(invoiceService.findInvoiceNOByInvoiceNO(InvoiceNO, count).toString());
	}
	
	@GetMapping("/Data/InvoiceHeader")
	public String getInvoiceHeader(
			@RequestParam(value = "InvoiceNO", required = false, defaultValue = "%%") String InvoiceNO) {
		return invoiceService.findInvoiceHeaderByInvoiceNO(InvoiceNO).toString();
	}
	@GetMapping("/Data/InvoiceBody")
	public String getInvoiceBody(
			@RequestParam(value = "InvoiceNO", required = false, defaultValue = "%%") String InvoiceNO) {
		return invoiceService.findInvoiceBodyByInvoiceNO(InvoiceNO).toString();
	}
}