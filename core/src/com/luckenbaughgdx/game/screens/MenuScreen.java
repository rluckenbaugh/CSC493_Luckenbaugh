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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.luckenbaughgdx.game.Assets;
import com.luckenbaughgdx.game.util.CharacterSkin;
import com.luckenbaughgdx.game.util.Constants;
import com.luckenbaughgdx.game.util.GamePreferences;

public class MenuScreen extends AbstractGameScreen
{

    private Stage stage;

    private Skin skinCanyonBunny;

    //menu
    private Image imgBackground;

    private Image imgLogo;

    private Image imgInfo;

    private Image imgCoins;

    private Image imgBunny;

    private Button btnMenuPlay;

    private Button btnMenuOptions;

    //options
    private Window winOptions;

    private TextButton btnWinOptSave;

    private TextButton btnWinOptCancel;

    private CheckBox chkSound;

    private Slider sldSound;

    private CheckBox chkMusic;

    private Slider sldMusic;

    private SelectBox<CharacterSkin> selCharSkin;

    private Image imgCharSkin;

    private CheckBox chkShowFpsCounter;

    //debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;

    private boolean debugEnabled = false;

    private float debugRebuildStage;

    private Skin skinLibgdx;

    public MenuScreen(Game game)
    {
        super(game);
    }

    private void loadSettings()
    {
        GamePreferences prefs = GamePreferences.instances;
        prefs.load();
        chkSound.setChecked(prefs.sound);
        sldSound.setValue(prefs.volSound);
        chkMusic.setChecked(prefs.music);
        sldMusic.setValue(prefs.volMusic);
        selCharSkin.setSelectedIndex(prefs.charSkin);
        onCharSkinSelected(prefs.charSkin);
        chkShowFpsCounter.setChecked(prefs.showFpsCounter);
    }

    private void saveSettings()
    {
        GamePreferences prefs = GamePreferences.instances;
        prefs.sound = chkSound.isChecked();
        prefs.volSound = sldSound.getValue();
        prefs.music = chkMusic.isChecked();
        prefs.volMusic = sldMusic.getValue();
        prefs.charSkin = selCharSkin.getSelectedIndex();
        prefs.showFpsCounter = chkShowFpsCounter.isChecked();
        prefs.save();
    }

    private void onCharSkinSelected(int index)
    {
        CharacterSkin skin = CharacterSkin.values()[index];
        imgCharSkin.setColor(skin.getColor());
    }

    private void onSaveClicked()
    {
        saveSettings();
        onCancelClicked();
    }

    private void onCancelClicked()
    {
        btnMenuPlay.setVisible(true);
        btnMenuOptions.setVisible(true);
        winOptions.setVisible(false);
    }

    private void rebuildStage()
    {
        skinCanyonBunny = new Skin(Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));

        skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

        //build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerObjects = buildObjectsLayer();
        Table layerLogos = buildLogosLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = builsOptionsWindowLayer();

        //assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerObjects);
        stack.add(layerLogos);
        stack.add(layerControls);
        stage.addActor(layerOptionsWindow);
    }

    private Table builsOptionsWindowLayer()
    {
        winOptions = new Window("Options", skinLibgdx);
        //Audio Settings: 
        winOptions.add(buildOptWinAudioSettings()).row();
        //character skin
        winOptions.add(buildOptWinSkinSelection()).row();
        //Debug
        winOptions.add(buildOptWinDebug()).row();
        //seperator and buttons
        winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);
        //make options window slightly transparent
        winOptions.setColor(1, 1, 1, 0.8f);
        //hide options window by default
        winOptions.setVisible(false);
        if (debugEnabled)
            winOptions.debug();
        //let table layout recalculate widget sizes and positions
        winOptions.pack();
        //move options window to bttom right corner
        winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50);
        return winOptions;
    }

    private Table buildLogosLayer()
    {
        Table layer = new Table();
        layer.left().top();
        //+Game logo
        imgLogo = new Image(skinCanyonBunny, "logo");
        layer.add(imgLogo);
        layer.row().expandY();
        //+ Info Logos
        imgInfo = new Image(skinCanyonBunny, "info");
        layer.add(imgInfo).bottom();
        if (debugEnabled)
            layer.debug();
        return layer;
    }

    private Table buildControlsLayer()
    {
        Table layer = new Table();
        layer.right().bottom();
        //+Play button
        btnMenuPlay = new Button(skinCanyonBunny, "play");
        layer.add(btnMenuPlay);
        btnMenuPlay.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                onPlayClicked();

            }
        });
        layer.row();
        // + Options Button
        btnMenuOptions = new Button(skinCanyonBunny, "options");
        layer.add(btnMenuOptions);
        btnMenuOptions.addListener(new ChangeListener()
        {

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                onOptionsClicked();

            }

        });
        if (debugEnabled)
            layer.debug();
        return layer;
    }

    protected void onOptionsClicked()
    {
        loadSettings();
        btnMenuPlay.setVisible(false);
        btnMenuOptions.setVisible(false);
        winOptions.setVisible(true);
    }

    protected void onPlayClicked()
    {
        game.setScreen(new GameScreen(game));

    }

    private Table buildObjectsLayer()
    {
        Table layer = new Table();
        //+ coins
        imgCoins = new Image(skinCanyonBunny, "coins");
        layer.addActor(imgCoins);
        imgCoins.setPosition(135, 80);
        //+ Bunny
        imgBunny = new Image(skinCanyonBunny, "bunny");
        layer.addActor(imgBunny);
        imgBunny.setPosition(355, 40);

        return layer;
    }

    private Table buildBackgroundLayer()
    {
        Table layer = new Table();
        //+ background
        imgBackground = new Image(skinCanyonBunny, "background");
        layer.add(imgBackground);
        return layer;
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
        stage.setDebugAll(true);
    }

    private Table buildOptWinAudioSettings()
    {

        Table tbl = new Table();
        //+Title audio"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Audio", skinLibgdx, "default-font", Color.ORANGE)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        //+Checkbox sound label sound slider
        chkSound = new CheckBox("", skinLibgdx);
        tbl.add(chkSound);
        tbl.add(new Label("Sound", skinLibgdx));
        sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldSound);
        tbl.row();
        //+ checkbox music label music volume slider
        chkMusic = new CheckBox("", skinLibgdx);
        tbl.add(chkMusic);
        tbl.add(new Label("Music", skinLibgdx));
        sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldMusic);
        tbl.row();
        return tbl;

    }

    private Table buildOptWinSkinSelection()
    {
        Table tbl = new Table();
        //Title character skin
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Character Skin", skinLibgdx, "default-font", Color.ORANGE)).colspan(2);
        tbl.row();
        //drop down box filled with skin items
        selCharSkin = new SelectBox<CharacterSkin>(skinLibgdx);
        selCharSkin.setItems(CharacterSkin.values());
        selCharSkin.addListener(new ChangeListener()
        {

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                onCharSkinSelected(((SelectBox<CharacterSkin>) actor).getSelectedIndex());
            }
        });
        tbl.add(selCharSkin).width(120).padRight(20);
        //Skin preview image
        imgCharSkin = new Image(Assets.instance.bunny.head);
        tbl.add(imgCharSkin).width(50).height(50);
        return tbl;
    }

    private Table buildOptWinDebug()
    {
        Table tbl = new Table();
        //title: DEbug
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Debug", skinLibgdx, "default-font", Color.RED)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        //Checkbox show fps counter label
        chkShowFpsCounter = new CheckBox("", skinLibgdx);
        tbl.add(new Label("Show FPS Counter", skinLibgdx));
        tbl.add(chkShowFpsCounter);
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
        //save button with even handler
        btnWinOptSave = new TextButton("Save", skinLibgdx);
        tbl.add(btnWinOptSave).padRight(30);
        btnWinOptSave.addListener(new ChangeListener()
        {

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                onSaveClicked();
            }
        });
        //cancel button with event handler
        btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
        tbl.add(btnWinOptCancel);
        btnWinOptCancel.addListener(new ChangeListener()
        {

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                onCancelClicked();
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
