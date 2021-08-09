package com.xpluscloud.moses_apc.getset;

public class CustomerData {
	
	private int o_id;
	private String ccode;
	private String oname;
	private String ostat;
	private String obday;
	private String ohobInt;
	private String ophone;
	private String oemail;
	private int ostatus;
	
	private String loan;
	private String average;
	private String route;
	private String freq;

	private String pname;
	private String pstat;
	private String pbday;
	private String phobInt;
	private String pphone;
	private String pemail;
	private int pstatus;
	
	
	public CustomerData(){
		
		this.ccode = "";
		
		this.oname	 = "";
		this.ostat 	 = "";
		this.obday 	 = "";
		this.ohobInt = "";
		this.ophone	 = "";
		this.oemail	 = "";
		this.ostatus = 0;
		
		this.loan 	 = "";
		this.average = "";
		this.route 	 = "";
		this.freq	 = "";

        this.pname	 = "";
        this.pstat 	 = "";
        this.pbday 	 = "";
        this.phobInt = "";
        this.pphone	 = "";
        this.pemail	 = "";
        this.pstatus = 0;
				
	}

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPstat() {
        return pstat;
    }

    public void setPstat(String pstat) {
        this.pstat = pstat;
    }

    public String getPbday() {
        return pbday;
    }

    public void setPbday(String pbday) {
        this.pbday = pbday;
    }

    public String getPhobInt() {
        return phobInt;
    }

    public void setPhobInt(String phobInt) {
        this.phobInt = phobInt;
    }

    public String getPphone() {
        return pphone;
    }

    public void setPphone(String pphone) {
        this.pphone = pphone;
    }

    public String getPemail() {
        return pemail;
    }

    public void setPemail(String pemail) {
        this.pemail = pemail;
    }

    public int getPstatus() {
        return pstatus;
    }

    public void setPstatus(int pstatus) {
        this.pstatus = pstatus;
    }

    public String getLoan() {
		return loan;
	}

	public void setLoan(String loan) {
		this.loan = loan;
	}

	public String getAverage() {
		return average;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	public int getO_id() {
		return o_id;
	}

	public void setO_id(int o_id) {
		this.o_id = o_id;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public String getOname() {
		return oname;
	}

	public void setOname(String oname) {
		this.oname = oname;
	}

	public String getOstat() {
		return ostat;
	}

	public void setOstat(String ostat) {
		this.ostat = ostat;
	}

	public String getObday() {
		return obday;
	}

	public void setObday(String obday) {
		this.obday = obday;
	}

	public String getOhobInt() {
		return ohobInt;
	}

	public void setOhobInt(String ohobInt) {
		this.ohobInt = ohobInt;
	}

	public String getOphone() {
		return ophone;
	}

	public void setOphone(String ophone) {
		this.ophone = ophone;
	}

	public String getOemail() {
		return oemail;
	}

	public void setOemail(String oemail) {
		this.oemail = oemail;
	}

	public int getOstatus() {
		return ostatus;
	}

	public void setOstatus(int ostatus) {
		this.ostatus = ostatus;
	}	

}
