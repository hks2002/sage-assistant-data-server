package sageassistant.model;

public class StockInfo extends AmodelTemplate {

	private static final long serialVersionUID = 1L;

	private String StockSite;
	private String PN;
	private Integer Qty;

	public String getStockSite() {
		return StockSite;
	}

	public void setStockSite(String stockSite) {
		StockSite = stockSite;
	}

	public String getPN() {
		return PN;
	}

	public void setPN(String pN) {
		PN = pN;
	}

	public Integer getQty() {
		return Qty;
	}

	public void setQty(Integer qty) {
		Qty = qty;
	}

}
