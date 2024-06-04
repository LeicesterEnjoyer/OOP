package oop;

import oop.evolution.World;

public class Main {
    public static void main(String[] args) {
        World world = World.getInstance();

        world.startWorld();
    }
}