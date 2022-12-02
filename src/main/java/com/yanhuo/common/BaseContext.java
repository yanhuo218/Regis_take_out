package com.yanhuo.common;


public class BaseContext {
    private static ThreadLocal<Long> thread = new ThreadLocal<>();

    public static void  setId(Long id){
        thread.set(id);
    }
    public static Long getId(){
        return thread.get();
    }

}
