package io.thefusion.puzzleattack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class pAttack extends ApplicationAdapter {
    private Camera cam;
    private Viewport viewport;
    private SpriteBatch batch;
    private ShapeRenderer sRender;
	private long points;
    private Grid grid;
    private int tileSize = 16;
    private Color tileColors[] = {
            new Color(0f,0f,0f,1f), // 0 = Black
            new Color(1f,0f,0f,1f), // 1 = Red
            new Color(0f,0f,1f,1f), // 2 = Blue
            new Color(1f,1f,0f,1f), // 3 = Yellow
            new Color(0f,1f,0f,1f), // 4 = Green
            new Color(1f,0f,1f,1f), // 5 = Pink
            new Color(0f,1f,1f,1f), // 6 = Cyan
    };
    private Texture textures[];
    private boolean moving;
    private int cursorPosition[] = new int[2];
    private int movedPosition[] = new int[2];
	@Override
	public void create () {
        textures = new Texture[]{
                new Texture(Gdx.files.internal("tile.png"))
        };
        grid = new Grid();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        batch = new SpriteBatch();
        sRender = new ShapeRenderer();
        cam = new OrthographicCamera(w,h);
        viewport = new FitViewport(tileSize*12,tileSize*12,cam); // 1 = 1 pixel
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean mouseMoved(int x, int y) {
                cursorPosition = getGridPos(x,y);
                return true;
            }
            @Override
            public boolean touchDragged (int x, int y, int pointer) {
                movedPosition = getGridPos(x,y);
                if(moving && ((movedPosition[0] != cursorPosition[0]) || (movedPosition[1] != cursorPosition[1]))) {
                    grid.move(cursorPosition[0],cursorPosition[1],movedPosition[0],movedPosition[1]);
                    cursorPosition = movedPosition;
                    //moving = false;
                }
                return true;
            }
            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                movedPosition = getGridPos(x,y);
                moving = true;
                return true;
            }
            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                moving = false;
                return true;
            }
        });
	}
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
    public int[] getGridPos(int x, int y) {
        int pos[] = new int[2];
        float screen[] = {viewport.getScreenWidth(),viewport.getScreenHeight()};
        pos[0] = (int)((float)(x-viewport.getLeftGutterWidth())/screen[0]*12)-3;
        pos[1] = (int)((float)(y-viewport.getTopGutterHeight()+(grid.shift*(screen[1]/12)))/screen[1]*12);
        pos[1] = -pos[1]+12;
        return pos;
    }
	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.position.set(tileSize * 3, tileSize * 7 - (int)(tileSize * grid.shift), 0);
        cam.update();
        //viewport.setScreenY((int)(tileSize*grid.shift));
        //cam.position.add(0,tileSize*grid.shift,0);
        //System.out.println(tileSize*grid.shift);
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        sRender.setProjectionMatrix(cam.combined);
        sRender.begin(ShapeRenderer.ShapeType.Line);
        for(int y = 0; y < grid.tiles[0].length; y++) {
            for(int x = 0; x < grid.tiles.length; x++) {
                Tile tile = grid.tiles[x][y];
                if(tile.active) {
                    Color color = new Color(tileColors[tile.type]);
                    color.sub(tile.cooldown,tile.cooldown,tile.cooldown, tile.cooldown);
                    if(y < 2)
                        color.sub(.5f, .5f, .5f,.5f);
                    if(tile.matched)
                        color.add(.5f,.5f,.5f,.5f);
                    batch.setColor(color);
                    batch.draw(textures[0], x * tileSize, y * tileSize);
                }
                if((cursorPosition[0] == x) && (cursorPosition[1] == y)) {
                    sRender.setColor(new Color(1f,1f,1f,.1f));
                    sRender.rect(x * tileSize, y * tileSize, tileSize, tileSize);
                }
                if((moving) && (movedPosition[0] == x) && (movedPosition[1] == y)) {
                    sRender.setColor(new Color(1f,1f,1f,.1f));
                    sRender.rect(x * tileSize+tileSize/4, y * tileSize+tileSize/4, tileSize/2, tileSize/2);
                }
            }
        }
        batch.end();
        sRender.end();
        grid.update();
	}
}
