package com.ekumfi.juice.util;


public class DrawingModel {
    int id;
    String name;
    byte[] drawing;

    public DrawingModel(int id, String name, byte[] drawing) {
        this.id = id;
        this.name = name;
        this.drawing = drawing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getDrawing() {
        return drawing;
    }

    public void setDrawing(byte[] drawing) {
        this.drawing = drawing;
    }
}
