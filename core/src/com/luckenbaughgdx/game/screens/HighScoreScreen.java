package com.luckenbaughgdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.luckenbaughgdx.game.util.CharacterSkin;
import com.luckenbaughgdx.game.util.Constants;
import com.luckenbaughgdx.game.util.GamePreferences;

public class HighScoreScreen extends AbstractGameScreen
{
    private Stage stage;

    private Skin skinCanyonBunny;

    private Button btnMenuPlay;

    //options
    private Window winOptions;

    private TextButton btnWinOptNext;

    //debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;

    private boolean debugEnabled = false;

    private float debugRebuildStage;

    private Skin skinLibgdx;
    
    GamePreferences prefs = GamePreferences.instances;

    public HighScoreScreen(Game game, int score)
    {
        super(game);
        prefs.addHighScore(score);
        //prefs.loadScores();
    }

    private void rebuildStage()
    {
        skinCanyonBunny = new Skin(Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));

        skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

        //build all layers

        Table layerHighScoreWindow = builsOptionsWindowLayer();

        //assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stage.addActor(layerHighScoreWindow);
    }

    private Table builsOptionsWindowLayer()
    {
        winOptions = new Window("High Scores", skinLibgdx);
        //Audio Settings: 
        winOptions.add(buildOptWinNextSettings()).row();
        //seperator and buttons
        winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);
        //make options window slightly transparent
        winOptions.setColor(1, 1, 1, 0.8f);
        //hide options window by default
        winOptions.setVisible(true);
        if (debugEnabled)
            winOptions.debug();
        //let table layout recalculate widget sizes and positions
        winOptions.pack();
        //move options window to bttom right corner
        winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50);
        return winOptions;
    }


    protected void onNextClicked()
    {
        game.setScreen(new MenuScreen(game));

    }

    @Override
    public void render(float deltaTime)
    {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (debugEnabled)
        {
            debugRebuildStage -= deltaTime;
            if (debugRebuildStage <= 0)
            {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuildStage();
            }
        }
        stage.act(deltaTime);
        stage.draw();
        stage.setDebugAll(false);
    }

    private Table buildOptWinNextSettings()
    {

        
        
        Table tbl = new Table();
        //+Title audio"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("High Score List", skinLibgdx, "default-font", Color.ORANGE)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        //high score 1
        tbl.add(new Label("1.", skinLibgdx));
        tbl.add(new Label(""+prefs.highScores.get(0),skinLibgdx));
        tbl.row();
        //high score 2
        tbl.add(new Label("2.", skinLibgdx));
        tbl.add(new Label(""+prefs.highScores.get(1),skinLibgdx));
        tbl.row();
        //high score 3
        tbl.add(new Label("3.", skinLibgdx));
        tbl.add(new Label(""+prefs.highScores.get(2),skinLibgdx));
        tbl.row();
        //high score 4
        tbl.add(new Label("4.", skinLibgdx));
        tbl.add(new Label(""+prefs.highScores.get(3),skinLibgdx));
        tbl.row();
        //high score 5
        tbl.add(new Label("5.", skinLibgdx));
        tbl.add(new Label(""+prefs.highScores.get(4),skinLibgdx));
        tbl.row();
        return tbl;

    }

    private Table buildOptWinButtons()
    {
        Table tbl = new Table();
        //seperator
        Label lbl = null;
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.75f, 0.75f, 0.75f, 1);
        lbl.setStyle(new LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
        tbl.row();
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.5f, 0.5f, 0.5f, 1);
        lbl.setStyle(new LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
        tbl.row();
        
        //cancel button with event handler
        btnWinOptNext = new TextButton("Next", skinLibgdx);
        tbl.add(btnWinOptNext);
        btnWinOptNext.addListener(new ChangeListener()
        {

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                onNextClicked();
            }
        });
        return tbl;
    }

    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide()
    {
        stage.dispose();
        skinCanyonBunny.dispose();
        skinLibgdx.dispose();
    }

    @Override
    public void show()
    {
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
    }

}
