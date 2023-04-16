package com.da.wrk.aircraft.web.model;

public enum Category {
  C ("Civil"), 
  M ("Militaire");

  private String value = "";

  Category(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  /*
  public static void main(String[] args) {
					
		for (Category cat : Category.values()) {
			System.out.println("cat: " + cat.ordinal() + " => " + cat.getValue());
    }	

    System.out.println("cat C => " + Category.valueOf("C").getValue());
    System.out.println("cat C => " + Category.valueOf("C"));

    System.out.println("cat C => " + Category.C.getValue());
    System.out.println("cat C => " + Category.C);		
  }*/
}
