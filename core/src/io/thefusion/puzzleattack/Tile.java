package io.thefusion.puzzleattack;

public class Tile {
  public int type = 0;
  public boolean active = false;
  public boolean matched = false;
  public float cooldown = 0f;
  public float flash = 0f;

  public Tile() {
  }

  public Tile(boolean activate) {
    if (activate) {
      active = true;
      type = (int) (Math.random() * 6) + 1;
    }
  }

  public void match() {
    if (!matched) {
      matched = true;
      flash = 1;
    }
  }
}
