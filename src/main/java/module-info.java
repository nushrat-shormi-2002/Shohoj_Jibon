/**
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
module com.threelegend.shohojjibon {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires jbcrypt;
    requires de.jensd.fx.glyphs.fontawesome;
    requires org.controlsfx.controls;
    requires org.apache.commons.lang3;

    opens com.threelegend.shohojjibon to javafx.fxml;
    exports com.threelegend.shohojjibon;
    
    opens com.threelegend.shohojjibon.controller to javafx.fxml;
    exports com.threelegend.shohojjibon.controller;
    
    opens com.threelegend.shohojjibon.model to javafx.base;
    exports com.threelegend.shohojjibon.model;
    
    exports com.threelegend.shohojjibon.dao;
    exports com.threelegend.shohojjibon.util;
}
