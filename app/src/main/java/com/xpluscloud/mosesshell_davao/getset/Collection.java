package com.xpluscloud.mosesshell_davao.getset;

public class Collection {
	
	private int id;
	private String date;
	private String invoiceno;
	private String ccode;
	private String customerName;
	private double cashamount;
	
	private double checkamount;
	private String checkno;
	
	private double cmamount;
	private String cmno;
	
	private double amount_dep;
	private String bankname;
	private String bankbranch;
	private String deptrno;
	
	private String orno;
	private int ncreason;
	private String remarks;
		
	private int status;
	
	public Collection(){
		this.date 		= "";
		this.invoiceno	="";
		this.ccode 		= "";
		this.customerName = "";
		this.cashamount	= 0.0;
		
		this.checkamount=0.0;
		this.checkno="";
		
		this.cmamount=0.0;
		this.cmno="";
		
		this.amount_dep=0.0;
		this.bankname="";
		this.bankbranch="";
		this.deptrno="";
		
		this.orno="";
		this.ncreason=0;
		
		this.remarks="";
		
		this.status 	= 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public double getCashamount() {
		return cashamount;
	}

	public void setCashamount(double cashamount) {
		this.cashamount = cashamount;
	}

	public double getCheckamount() {
		return checkamount;
	}

	public void setCheckamount(double checkamount) {
		this.checkamount = checkamount;
	}

	public String getCheckno() {
		return checkno;
	}

	public void setCheckno(String checkno) {
		this.checkno = checkno;
	}

	public double getCmamount() {
		return cmamount;
	}

	public void setCmamount(double cmamount) {
		this.cmamount = cmamount;
	}

	public String getCmno() {
		return cmno;
	}

	public void setCmno(String cmno) {
		this.cmno = cmno;
	}

	public double getAmount_dep() {
		return amount_dep;
	}

	public void setAmount_dep(double amount_dep) {
		this.amount_dep = amount_dep;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getBankbranch() {
		return bankbranch;
	}

	public void setBankbranch(String bankbranch) {
		this.bankbranch = bankbranch;
	}

	public String getDeptrno() {
		return deptrno;
	}

	public void setDeptrno(String deptrno) {
		this.deptrno = deptrno;
	}

	public String getOrno() {
		return orno;
	}

	public void setOrno(String orno) {
		this.orno = orno;
	}

	public int getNcreason() {
		return ncreason;
	}

	public void setNcreason(int ncreason) {
		this.ncreason = ncreason;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}