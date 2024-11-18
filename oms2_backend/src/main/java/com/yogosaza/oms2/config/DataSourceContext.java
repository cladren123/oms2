//package com.yogosaza.oms2.config;
//
//public class DataSourceContext {
//
//    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();
//
//    public static void setDataSource(String dataSourceType) {
//        CONTEXT.set(dataSourceType);
//    }
//
//    public static String getCurrentDataSource() {
//        return CONTEXT.get();
//    }
//
//    public static void clear() {
//        CONTEXT.remove();
//    }
//}
