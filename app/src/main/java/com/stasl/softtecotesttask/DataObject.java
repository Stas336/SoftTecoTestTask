package com.stasl.softtecotesttask;

import java.util.ArrayList;

public class DataObject
{
    private ArrayList<String> names;
    private int LIMIT = 6;
    public DataObject(ArrayList<String> names) {
        this.names = names;
    }
    public String getName(int pos) {
        return names.get(pos);
    }
    public void setName(String name, int pos) {
        names.set(pos, name);
    }
    public void addName(String name)
    {
        if (names.size() < 6)
        {
            names.add(name);
        }
    }
    public int size()
    {
        return names.size();
    }
}
