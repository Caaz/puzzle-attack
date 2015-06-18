package io.thefusion.puzzleattack;

public class Grid {
  public Tile tiles[][] = new Tile[6][12];
  private boolean rising = false;
  public float shift = 0.5f;
  private float speed = .005f;
  private float cooldownRate = .04f;

  public Grid() {
    // Brand new grid,set up random colors
    for (int y = 0; y < tiles[0].length; y++) {
      for (int x = 0; x < tiles.length; x++) {
        //
        if (y < 5)
          tiles[x][y] = new Tile(true);
        else
          tiles[x][y] = new Tile();
      }
    }
  }

  public boolean move(int x, int y, int tX, int tY) {
    if ((x >= 0) && (x < tiles.length) && (y >= 2) && (y < tiles[0].length)) {
      if ((tX >= 0) && (tX < tiles.length) && (tY >= 2) && (tY < tiles[0].length)) {
        if ((tiles[x][y].cooldown <= 0) && (tiles[tX][tY].cooldown <= 0)) {
          Tile hold = tiles[x][y];
          tiles[x][y] = tiles[tX][tY];
          tiles[tX][tY] = hold;
          tiles[x][y].cooldown = 1;
          tiles[tX][tY].cooldown = 1;
          check();
          return true;
        } else {
          System.out.println("Tiles haven't cooled!");
          return false;
        }
      }
    }
    System.out.println("Out of bounds move!");
    return false;
  }

  public void check() {
    int curCombo;
    int count;
    for (int x = 0; x < tiles.length; x++) {
      boolean falling = true;
      while (falling) {
        falling = false;
        for (int y = 2; y < tiles[0].length; y++) {
          if (tiles[x][y].active && !tiles[x][y - 1].active) {
            tiles[x][y - 1] = tiles[x][y];
            tiles[x][y] = new Tile();
            falling = true;
          }
        }
      }
      curCombo = 0;
      count = 1;
      for (int y = 2; y < tiles[0].length; y++) {
        if ((curCombo != 0) && (curCombo == tiles[x][y].type)) {
          count++;
          if (count == 3) {
            tiles[x][y - 2].match();
            tiles[x][y - 1].match();
          }
          if (count >= 3)
            tiles[x][y].match();
        } else {
          curCombo = tiles[x][y].type;
          count = 1;
        }
      }
    }
    for (int y = 2; y < tiles[0].length; y++) {
      curCombo = 0;
      count = 1;
      for (int x = 0; x < tiles.length; x++) {
        if ((curCombo != 0) && (curCombo == tiles[x][y].type)) {
          count++;
          if (count == 3) {
            tiles[x - 2][y].match();
            tiles[x - 1][y].match();
          }
          if (count >= 3)
            tiles[x][y].match();
        } else {
          curCombo = tiles[x][y].type;
          count = 1;
        }
      }
    }
  }

  public void update() {
    shift += speed;

    boolean collapsed = false;
    for (int x = 0; x < tiles.length; x++) {
      for (int y = 2; y < tiles[0].length; y++) {
        if (tiles[x][y].cooldown > 0) {
          tiles[x][y].cooldown -= cooldownRate;
        }
        if (tiles[x][y].flash > 0) {
          tiles[x][y].flash -= cooldownRate;
        }
        if ((tiles[x][y].matched) && (tiles[x][y].flash <= 0)) {
          tiles[x][y] = new Tile(false);
          collapsed = true;
        }
      }
    }
    if (collapsed)
      check();

    if (shift > 1) {
      shift -= 1;
      for (int y = tiles[0].length - 1; y >= 0; y--) {
        for (int x = 0; x < tiles.length; x++) {
          //
          if (y > 0)
            tiles[x][y] = tiles[x][y - 1];
          else
            tiles[x][y] = new Tile(true);
        }
      }
      check();
    }
  }
}
