package io.thefusion.puzzleattack;

public class Tile {
    public int type = 0;
    public boolean active = false;
    public boolean matched = false;
    public float cooldown = 0f;
    public Tile() { active = false; type = 0; matched = false; }
    public Tile(boolean activate) {
        if(activate) {
            active = true;
            type = (int)(Math.random()*6)+1;
        }
    }
}
