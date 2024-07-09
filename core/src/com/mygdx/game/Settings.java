package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.Music.GameMusic;

import org.w3c.dom.Text;

import java.io.File;

public class Settings implements Scene {
    private Texture backgroundTexture = new Texture(Gdx.files.internal("background.png"));
    private SpriteBatch backgroundSprite;

    public static Stage stage;

    private static Settings object;
    private BitmapFont descriptionFont;

    private Label.LabelStyle descriptionStyle;
    Label.LabelStyle simpleStyle;

    private Racing racing;

    private BitmapFont simpleFont;

    private final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    private final int SCREEN_HEIGHT = Gdx.graphics.getHeight();

    private boolean volume = true;
    private boolean accelerometerFlag = false;

    private void ReadSettingFromFile() {
        FileHandle handle = null;
        String line = "";
        try {
            handle = Gdx.files.local("Settings/settings.txt");
            line = handle.readString();
        } catch (GdxRuntimeException ex) {
            handle = Gdx.files.internal("Settings/settings.txt");
            line = handle.readString();
        } finally {
            String[] lines = line.split("\r\n");
            GameMusic music = GameMusic.MusicInitialize();

            String musicPath = lines[0].split("=")[1];
            Music gamemusic = null;
            if (musicPath.equals("standart")) {
                gamemusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/game.mp3"));
            } else {
                try {
                    gamemusic = Gdx.audio.newMusic(Gdx.files.absolute(musicPath));
                    music.setGameMusicPath(musicPath);
                } catch (GdxRuntimeException ex) {
                    gamemusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/game.mp3"));
                    music.setGameMusicPath("standart");
                }
            }
            music.setGameMusic(gamemusic);

            accelerometerFlag = Boolean.parseBoolean(lines[1].split("=")[1]);
            volume = Boolean.parseBoolean(lines[2].split("=")[1]);
            music.setVolume(volume);
        }
    }

    private void WriteSettingsToFile() {
        String musicpath = GameMusic.MusicInitialize().getGameMusicPath();
        FileHandle handle = Gdx.files.local("Settings/settings.txt");
        String writeLine = "musicPath=" + musicpath + "\r\n" +
                "accelerometer=" + accelerometerFlag + "\r\n" +
                "volume=" + volume;
        handle.writeString(writeLine, false);
    }

    private Settings() {
        backgroundSprite = new SpriteBatch();
        stage = new Stage();

        ReadSettingFromFile();

        InitializeFontAndStyles();
        InitTextures();
        stage.addActor(InitializeContent());
    }

    public static Settings InitializeSettings(Scene currentScene, Stage currentStage) {
        if (object == null) {
            object = new Settings();
        }
        object.InitBackButton(currentScene, currentStage);
        return object;
    }

    private float scaleFont = (float) SCREEN_WIDTH / 2400;

    private void InitializeFontAndStyles() {
        descriptionFont = new BitmapFont(Gdx.files.internal("font.fnt"));
        descriptionFont.getData().setScale(2 * scaleFont, 2 * scaleFont);
        descriptionStyle = new Label.LabelStyle(descriptionFont, Color.WHITE);

        simpleFont = new BitmapFont(Gdx.files.internal("font.fnt"));
        simpleFont.getData().setScale(scaleFont, scaleFont);
        simpleStyle = new Label.LabelStyle(simpleFont, Color.WHITE);
    }

    private Texture searchTexture;
    private Texture searchHoverTexture;
    private Texture volumeOnTexture;
    private Texture volumeOnHoverTexture;
    private Texture volumeOffTexture;
    private Texture volumeOffHoverTexture;
    private Texture resetTexture;
    private Texture resetHoverTexture;
    private Texture okTexture;
    private Texture noTexture;

    private void InitTextures() {
        searchTexture = new Texture(Gdx.files.internal("Settings/search.png"));
        searchHoverTexture = new Texture(Gdx.files.internal("Settings/search_hover.png"));
        volumeOnTexture = new Texture(Gdx.files.internal("Settings/volume_on.png"));
        volumeOnHoverTexture = new Texture(Gdx.files.internal("Settings/volume_on_hover.png"));
        volumeOffTexture = new Texture(Gdx.files.internal("Settings/volume_off.png"));
        volumeOffHoverTexture = new Texture(Gdx.files.internal("Settings/volume_off_hover.png"));
        resetTexture = new Texture(Gdx.files.internal("Settings/reset.png"));
        resetHoverTexture = new Texture(Gdx.files.internal("Settings/reset_hover.png"));
        okTexture = new Texture(Gdx.files.internal("Settings/ok.png"));
        noTexture = new Texture(Gdx.files.internal("Settings/no.png"));
    }

    private static Texture backTexture = new Texture(Gdx.files.internal("Store/left.png"));
    private static Texture backHoverTexture = new Texture(Gdx.files.internal("Store/hover_left.png"));

    private void InitBackButton(Scene scene, Stage stage) {
        for (int i = 0; i < Settings.stage.getActors().size; i++) {
            if (Settings.stage.getActors().get(i).getName() != null && Settings.stage.getActors().get(i).getName().equals("back")) {
                Settings.stage.getActors().removeIndex(i);
                break;
            }
        }
        Button back = new Button(new TextureRegionDrawable(backTexture),
                new TextureRegionDrawable(backHoverTexture));
        back.setSize(SCREEN_HEIGHT / 6f, SCREEN_HEIGHT / 6f);
        back.setPosition(50, SCREEN_HEIGHT - back.getHeight() - 50);
        back.setName("back");
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                racing.setCurrentScene(scene);
                Gdx.input.setInputProcessor(stage);
            }
        });
        Settings.stage.addActor(back);
    }

    private void setUserMusic() {
        new Thread(() -> {
            String permissionStatus = null;
            racing.activity.checkPermission();
            boolean permissionOk = false;
            try {
                while (permissionStatus == null) {
                    permissionStatus = racing.activity.getAudioPermission();
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (permissionStatus.equals("OK")) {
                permissionOk = true;
            }
            if (!permissionOk) {
                return;
            }
            String path;
            racing.activity.openFileChooser();
            do {
                try {
                    path = racing.activity.getAudioFilePath();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } while (path == null);
            racing.activity.setPathNull();
            if (path.equals("Closed")) {
                System.out.println(path);
            }
            if (path.equals("Not found")) {
                return;
            }
            if (new File(path).exists()) {
                Music music = Gdx.audio.newMusic(Gdx.files.absolute(path));
                GameMusic gameMusic = GameMusic.MusicInitialize();
                gameMusic.setGameMusic(music);
                gameMusic.setGameMusicPath(path);
                WriteSettingsToFile();
            }
        }).start();
    }

    private Table InitializeContent() {
        for (int i = 0; i < Settings.stage.getActors().size; i++) {
            if (Settings.stage.getActors().get(i).getName() != null && Settings.stage.getActors().get(i).getName().equals("settingTable")) {
                Settings.stage.getActors().removeIndex(i);
                break;
            }
        }

        Label descriptionLabel = new Label("Настройки", descriptionStyle);
        descriptionLabel.setPosition(SCREEN_WIDTH / 2f - descriptionLabel.getWidth() / 2f, SCREEN_HEIGHT - descriptionLabel.getHeight() - 50);
        stage.addActor(descriptionLabel);

        float width = SCREEN_WIDTH / 3f;
        float height = SCREEN_HEIGHT / 5f;
        int pad = 60;

        Table table = new Table();
        table.setName("settingTable");

        Button searchMusicButton = new Button(new TextureRegionDrawable(searchTexture), new TextureRegionDrawable(searchHoverTexture));
        Button volumeButton;
        if (volume) {
            volumeButton = new Button(new TextureRegionDrawable(volumeOnTexture), new TextureRegionDrawable(volumeOnHoverTexture));
            volumeButton.setName("ON");
        } else {
            volumeButton = new Button(new TextureRegionDrawable(volumeOffTexture), new TextureRegionDrawable(volumeOffHoverTexture));
            volumeButton.setName("OFF");
        }

        volumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (volumeButton.getName().equals("ON")) {
                    GameMusic music = GameMusic.MusicInitialize();
                    volume = false;
                    music.setVolume(volume);
                    WriteSettingsToFile();
                    stage.addActor(InitializeContent());
                } else if (volumeButton.getName().equals("OFF")) {
                    GameMusic music = GameMusic.MusicInitialize();
                    volume = true;
                    music.setVolume(volume);
                    WriteSettingsToFile();
                    stage.addActor(InitializeContent());
                }
            }
        });

        searchMusicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setUserMusic();
            }
        });

        Button resetButton = new Button(new TextureRegionDrawable(resetTexture), new TextureRegionDrawable(resetHoverTexture));

        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMusic music = GameMusic.MusicInitialize();
                music.setGameMusic(music.getDefaultGameMusic());
                music.setGameMusicPath("standart");
                WriteSettingsToFile();
            }
        });
        Label searchMusicLabel = new Label("Выбрать свою музыку во время игры", simpleStyle);
        Label resetMusicLabel = new Label("Сброс", simpleStyle);

        table.add(searchMusicLabel).width(searchMusicLabel.getWidth()).height(searchMusicLabel.getHeight());
        table.add(resetMusicLabel).width(resetMusicLabel.getWidth()).height(resetMusicLabel.getHeight()).padLeft(pad).row();

        table.add(searchMusicButton).width(searchMusicLabel.getWidth()).height(height);
        table.add(resetButton).width(height).height(height).padLeft(pad).row();

        Label volumeLabel;
        if (volume) {
            volumeLabel = new Label("Выключить звук", simpleStyle);
        } else {
            volumeLabel = new Label("Включить звук", simpleStyle);
        }

        Table helpTable = new Table();
        helpTable.add(volumeLabel).width(volumeLabel.getWidth()).height(volumeLabel.getHeight()).row();
        helpTable.add(volumeButton).width(searchMusicLabel.getWidth()).height(height).row();
        table.add(helpTable).padTop(pad / 2f).row();

        Label accelerometerLabel = new Label("Управление акселерометром", simpleStyle);
        Button accelerometerEnableButton;
        if (accelerometerFlag) {
            accelerometerEnableButton = new Button(new TextureRegionDrawable(okTexture));
            accelerometerEnableButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    accelerometerFlag = false;
                    WriteSettingsToFile();
                    stage.addActor(InitializeContent());
                }
            });
        } else {
            accelerometerEnableButton = new Button(new TextureRegionDrawable(noTexture));
            accelerometerEnableButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    accelerometerFlag = true;
                    WriteSettingsToFile();
                    stage.addActor(InitializeContent());
                }
            });
        }
        table.add(accelerometerLabel).width(searchMusicLabel.getWidth()).height(accelerometerLabel.getHeight()).padTop(height/2f);
        table.add(accelerometerEnableButton).width(height/2f).height(height/2f).padTop(height/2f);

        table.setSize(searchMusicLabel.getWidth(), searchMusicLabel.getHeight() + height);
        table.setPosition(SCREEN_WIDTH / 2f - table.getWidth() / 2f, SCREEN_HEIGHT / 3f);
        long after = System.currentTimeMillis();
        return table;
    }

    public void setRacing(Racing racing) {
        this.racing = racing;
    }

    @Override
    public void dispose() {
        stage.dispose();
        noTexture.dispose();
        okTexture.dispose();
        resetTexture.dispose();
        searchTexture.dispose();
        resetHoverTexture.dispose();
        searchHoverTexture.dispose();
        volumeOffHoverTexture.dispose();
        volumeOffTexture.dispose();
        volumeOnTexture.dispose();
        volumeOnHoverTexture.dispose();
        backHoverTexture.dispose();
        backTexture.dispose();
        backTexture.dispose();
        backgroundTexture.dispose();
        backgroundSprite.dispose();
    }

    @Override
    public void render() {
        backgroundSprite.begin();
        backgroundSprite.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundSprite.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void create() {

    }

    public boolean isAccelerometerFlag() {
        return accelerometerFlag;
    }
}
