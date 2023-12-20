package agh.ics.oop.model;

import agh.ics.oop.model.worldElements.WorldElement;

public class Grass implements WorldElement {
    private final Vector2d position;

    public Grass(Vector2d position){
        this.position = position;
    }

    @Override
    public String toString(){
        return "*";
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return getPosition().equals(position);
    }

}