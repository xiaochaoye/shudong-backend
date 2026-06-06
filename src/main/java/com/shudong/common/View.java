package com.shudong.common;

public class View {
    // 基础视图 - 用于列表展示、随机获取等场景
    public static class Basic {}
    
    // 详细视图 - 用于详情页面，包含用户信息
    public static class Detail extends Basic {}
    
    // 我的视图 - 用于"我的愿望"页面，包含个性化信息
    public static class My extends Detail {}
}
