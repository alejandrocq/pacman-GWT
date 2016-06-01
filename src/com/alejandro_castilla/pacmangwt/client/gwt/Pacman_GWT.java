package com.alejandro_castilla.pacmangwt.client.gwt;

import com.alejandro_castilla.pacmangwt.client.jre.Map;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

public class Pacman_GWT implements EntryPoint {

    private final String menuBarHolder = "menuBarContainer";
    private MenuBar menuBar;

    private final String canvasHolder = "canvasContainer";
    private final int canvasWidth = 800;
    private final int canvasHeight = 600;
    private Canvas canvas;
    private Context2d context2D;

    private Map maze = new Map();

    private final CssColor mazeColor = CssColor.make("blue");
    private final CssColor canvasColor = CssColor.make("black");

    public void onModuleLoad() {

        createMenuBar();
        createCanvas();

        menuBar = createMenuBar();
        RootPanel.get(menuBarHolder).add(menuBar);
        RootPanel.get(canvasHolder).add(canvas);

        drawMaze();
    }

    private MenuBar createMenuBar() {
        Command menuBarHandler = new Command() {
            @Override
            public void execute() {
                Window.alert("Has pulsado una opción del menú.");
            }
        };

        MenuBar gameBar = new MenuBar(true);
        gameBar.addItem("Nuevo", menuBarHandler);
        gameBar.addItem("Pausar", menuBarHandler);

        MenuBar helpBar = new MenuBar(true);
        helpBar.addItem("Acerca de", menuBarHandler);

        MenuBar rootMenuBar = new MenuBar();
        rootMenuBar.addItem("Juego", gameBar);
        rootMenuBar.addItem("Ayuda", helpBar);

        return rootMenuBar;
    }

    private void createCanvas() {
        canvas = canvas.createIfSupported();

        canvas.setWidth(canvasWidth + "px");
        canvas.setHeight(canvasHeight + "px");
        canvas.setCoordinateSpaceWidth(canvasWidth);
        canvas.setCoordinateSpaceHeight(canvasHeight);
    }

    private void drawMaze() {
        int cellWidth = Math.min(canvasWidth / maze.getWidth(), canvasHeight / maze.getHeight());
        int x, y, offset = (canvasWidth - (maze.getWidth() * cellWidth)) / 2;

        context2D = canvas.getContext2d();
        context2D.beginPath();
        context2D.setFillStyle(canvasColor);
        context2D.fillRect(0, 0, canvasWidth, canvasWidth);
        context2D.closePath();

        for (x = 0; x < maze.getWidth(); x++) {
            for (y = 0; y < maze.getHeight(); y++) {
                switch (maze.getCellAt(x, y)) {
                    case Map.RECTANGLE_UP:
                        context2D.beginPath();
                        context2D.setFillStyle(mazeColor);
                        context2D.fillRect(offset + x * cellWidth, y * cellWidth, cellWidth, cellWidth / 2);
                        context2D.closePath();
                        break;
                    case Map.RECTANGLE_DOWN:
                        context2D.beginPath();
                        context2D.setFillStyle(mazeColor);
                        context2D.fillRect(offset + x * cellWidth, y * cellWidth + cellWidth / 2, cellWidth,
                                cellWidth / 2);
                        context2D.closePath();
                        break;
                    case Map.RECTANGLE_LEFT:
                        context2D.beginPath();
                        context2D.setFillStyle(mazeColor);
                        context2D.fillRect(offset + x * cellWidth, y * cellWidth, cellWidth / 2, cellWidth);
                        context2D.closePath();
                        break;
                    case Map.RECTANGLE_RIGHT:
                        context2D.beginPath();
                        context2D.setFillStyle(mazeColor);
                        context2D.fillRect(offset + x * cellWidth + cellWidth / 2, y * cellWidth, cellWidth / 2,
                                cellWidth);
                        context2D.closePath();
                        break;
                    case Map.UPPER_LEFT_CORNER:
                        context2D.beginPath();
                        context2D.setFillStyle(mazeColor);
                        context2D.moveTo(offset + x * cellWidth, y * cellWidth);
                        context2D.lineTo(offset + x * cellWidth, y * cellWidth + cellWidth / 2);
                        context2D.lineTo(offset + x * cellWidth + cellWidth / 2, y * cellWidth);
                        context2D.moveTo(offset + x * cellWidth + cellWidth / 2, y * cellWidth);
                        context2D.lineTo(offset + x * cellWidth, y * cellWidth + cellWidth / 2);
                        context2D.fill();
                        context2D.closePath();
                        break;
                    case Map.BOTTOM_LEFT_CORNER:
                        //TODO Complete this
                        break;
                    case Map.UPPER_LEFT_BIG_CORNER:
                        context2D.beginPath();
                        context2D.setFillStyle(mazeColor);
                        context2D.fillRect(offset + x * cellWidth, y * cellWidth, cellWidth, cellWidth);
                        context2D.setFillStyle(canvasColor);
                        context2D.moveTo(offset + x * cellWidth, y * cellWidth);
                        context2D.lineTo(offset + x * cellWidth, y * cellWidth + cellWidth / 2);
                        context2D.lineTo(offset + x * cellWidth + cellWidth / 2, y * cellWidth);
                        context2D.moveTo(offset + x * cellWidth + cellWidth / 2, y * cellWidth);
                        context2D.lineTo(offset + x * cellWidth, y * cellWidth + cellWidth / 2);
                        context2D.fill();
                        context2D.closePath();
                        break;
                }
            }
        }
    }
}
