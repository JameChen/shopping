package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShipItem implements Serializable {
    /**
     * 
     */
    private static final long     serialVersionUID = 1L;
    public String                 Name;
    public long                   ItemID;
    public String                 Cover;
    public List<ShipItem.Product> Products         = new ArrayList<ShipItem.Product>();

    public static class Product {
        public String               ColorName;
        private List<ShipItem.Size> Size = new ArrayList<ShipItem.Size>();

        public String getSize() {
            StringBuilder builder = new StringBuilder();
            for (Size s : this.Size) {
                builder.append(s.SizeName).append(" ").append(s.TotalQty).append("      ");
            }
            return builder.toString();
        }
    }
    public static class Size {
        public String SizeName;
        public int    TotalQty;
    }
}
