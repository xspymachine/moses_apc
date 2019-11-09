package com.xpluscloud.mosesshell_davao;


import com.xpluscloud.mosesshell_davao.getset.Delivery;

import java.util.ArrayList;
import java.util.List;

public class GroupDelivery {

  public String string;
  public final List<Delivery> children = new ArrayList<Delivery>();

  public GroupDelivery(String string) {
    this.string = string;
  }

} 