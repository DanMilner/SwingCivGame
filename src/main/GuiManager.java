package main;

import map.Constructable;
import map.Coordinates;
import map.Tile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class GuiManager extends JFrame implements ActionListener {
    private final int MAPSIZE;
    private BoardButton[][] boardButtons;
    private ArrayList<UiButton> uiButtons;
    private GameController gameController;
    private Coordinates currentCoordinates;
    private Point origin;
    private UiTextManager uiTextManager;
    private boolean unitSelected;

    GuiManager(GameController gameController, int MAPSIZE) {
        this.MAPSIZE = MAPSIZE;
        this.gameController = gameController;
        boardButtons = new BoardButton[MAPSIZE][MAPSIZE];
        uiButtons = new ArrayList<>();
        currentCoordinates = new Coordinates(0, 0);
        unitSelected = false;

        BoardPanel boardPanel = new BoardPanel(MAPSIZE);
        UiPanel uiPanel = new UiPanel();
        uiTextManager = new UiTextManager(uiPanel);

        createUIButtons(uiPanel);

        createBoardButtons(boardPanel);

        FrameHandler.createAndSetupFrameAndScrollPane(uiPanel, boardPanel);

        uiTextManager.updateUI(gameController.getCurrentPlayer());
    }

    private void createUIButtons(JPanel uiPanel) {
        final int COLUMN_OFFSET = 120;
        final int FIRST_COLUMN = 300;
        final int FIRST_ROW = 10;
        final int LAST_COLUMN = 900;
        final int LAST_ROW = 70;

        int xPosition = FIRST_COLUMN;
        int yPosition = FIRST_ROW;

        JButton endTurn = new JButton("End Turn");
        endTurn.setBounds(1830, 0, 90, 125);
        endTurn.addActionListener(arg0 -> {
            gameController.endTurn();
            unitSelected = false;
            updateAllButtonIconsAndBorders();
            hideUIButtons();
            uiTextManager.updateUI(gameController.getCurrentPlayer());
        });
        uiPanel.add(endTurn, BorderLayout.LINE_END);

        for (int i = 0; i < 12; i++) {
            uiButtons.add(new UiButton());
            uiButtons.get(i).setBounds(xPosition, yPosition, 110, 50);
            uiButtons.get(i).setVisible(false);
            uiButtons.get(i).setBackground(Color.white);
            uiPanel.add(uiButtons.get(i), BorderLayout.CENTER);
            xPosition = xPosition + COLUMN_OFFSET;
            if (xPosition > LAST_COLUMN) {
                yPosition = LAST_ROW;
                xPosition = FIRST_COLUMN;
            }
        }

        for (int i = 0; i < uiButtons.size(); i++) {
            int buttonNum = i;
            uiButtons.get(i).addActionListener(arg0 -> uiButtonAction(buttonNum));
        }
    }

    private void uiButtonAction(int ButtonNum) {
        Constructable constructable = uiButtons.get(ButtonNum).getConstructable();

        Coordinates coordinates = new Coordinates(currentCoordinates.x, currentCoordinates.y);

        gameController.buttonClicked(coordinates, constructable);

        updateLocalButtonIconsAndBorders();
        uiTextManager.updateUI(gameController.getCurrentPlayer());

        if (unitSelected) {
            unitSelected = false;
            hideUIButtons();
        } else {
            setButtonText();
        }
    }

    private void updateAllButtonIconsAndBorders() {
        updateBoardButtonIconsAndBorders(0, 0, MAPSIZE, MAPSIZE);
    }

    private void updateLocalButtonIconsAndBorders() {
        final int UPDATE_AREA = 10;
        int xHigh = Math.min(currentCoordinates.x + UPDATE_AREA, MAPSIZE);
        int yHigh = Math.min(currentCoordinates.y + UPDATE_AREA, MAPSIZE);
        int xLow = Math.max(currentCoordinates.x - UPDATE_AREA, 0);
        int yLow = Math.max(currentCoordinates.y - UPDATE_AREA, 0);
        updateBoardButtonIconsAndBorders(xLow, yLow, xHigh, yHigh);
    }

    private void updateBoardButtonIconsAndBorders(int xLow, int yLow, int xHigh, int yHigh) {
        for (int x = xLow; x < xHigh; x++) {
            for (int y = yLow; y < yHigh; y++) {
                BoardButton buttonBeingUpdated = boardButtons[x][y];
                buttonBeingUpdated.setBorder(null);
                Coordinates coordinates = new Coordinates(x, y);

                Tile currentTile = gameController.getMap().getTile(coordinates);

                ImageIcon icon = gameController.getTileImage(coordinates);
                buttonBeingUpdated.setIcon(icon);

                setButtonBorders(currentTile, buttonBeingUpdated);
            }
        }
    }

    private void hideUIButtons() {
        for (JButton UiButton : uiButtons) {
            UiButton.setVisible(false);
        }
    }

    private void setButtonText() {
        ArrayList<Constructable> buttonsToBuild = gameController.getTileButtonList(unitSelected, currentCoordinates);
        if (buttonsToBuild == null)
            return;

        int index = 0;
        for (Constructable constructable : buttonsToBuild) {
            uiButtons.get(index).setText(constructable.toString().toLowerCase());
            uiButtons.get(index).setConstructable(constructable);
            uiButtons.get(index).setVisible(true);
            if (constructable != Constructable.ROAD)
                colourUIButtons(index, constructable);
            index++;
        }
    }

    private void colourUIButtons(int index, Constructable constructable) {
        if (gameController.checkAvailableResources(constructable, unitSelected)) {
            uiButtons.get(index).setBackground(Color.white);
            uiButtons.get(index).setEnabled(true);
        } else {
            uiButtons.get(index).setBackground(Color.red);
            uiButtons.get(index).setEnabled(false);
        }
    }

    private void setButtonBorders(Tile currentTile, BoardButton buttonBeingUpdated) {
        Color borderColour;

        if (currentTile.hasOwner()) {
            borderColour = currentTile.getOwner().getColour();
            createLargerBorder(borderColour, buttonBeingUpdated);
            if (currentTile.getResource().isInUse()) {
                createSmallBorder(borderColour, buttonBeingUpdated);
            } else if (currentTile.hasBuildingWithCityConnection()) {
                createSmallBorder(Color.green, buttonBeingUpdated);
            }
        }

        if (currentTile.hasUnit()) {
            borderColour = currentTile.getUnit().getOwner().getColour();
            createSmallBorder(borderColour, buttonBeingUpdated);
        }
    }

    private void createSmallBorder(Color borderColour, BoardButton buttonBeingUpdated) {
        final int BORDER_THICKNESS = 1;
        buttonBeingUpdated.setBorder(BorderFactory.createLineBorder(borderColour, BORDER_THICKNESS));
    }

    private void createLargerBorder(Color borderColour, BoardButton buttonBeingUpdated) {
        Coordinates coord = buttonBeingUpdated.getCoordinates();

        buttonBeingUpdated.setBorder(BorderFactory.createMatteBorder(
                gameController.getMap().borderRequired(coord, new Coordinates(coord.x, coord.y - 1)) ? 3 : 0,
                gameController.getMap().borderRequired(coord, new Coordinates(coord.x - 1, coord.y)) ? 3 : 0,
                gameController.getMap().borderRequired(coord, new Coordinates(coord.x, coord.y + 1)) ? 3 : 0,
                gameController.getMap().borderRequired(coord, new Coordinates(coord.x + 1, coord.y)) ? 3 : 0,
                borderColour));
    }

    private void createBoardButtons(BoardPanel boardPanel) {
        final int BUTTON_SIZE = 50;
        final int START_POSITION = 0;

        int xPosition = START_POSITION;
        int yPosition;

        //create board elements;
        for (int x = 0; x < MAPSIZE; x++) {
            yPosition = START_POSITION;
            for (int y = 0; y < MAPSIZE; y++) {
                Coordinates coordinates = new Coordinates(x, y);
                BoardButton newBoardButton = new BoardButton(coordinates);
                boardButtons[x][y] = newBoardButton;
                Tile currentTile = gameController.getMap().getTile(coordinates);

                newBoardButton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        origin = new Point(e.getPoint());
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }
                });
                newBoardButton.addMouseMotionListener(new MouseMotionListener() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if (origin != null) {
                            JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, boardPanel);
                            if (viewPort != null) {
                                int deltaX = origin.x - e.getX();
                                int deltaY = origin.y - e.getY();

                                Rectangle view = viewPort.getViewRect();
                                view.x += deltaX;
                                view.y += deltaY;

                                boardPanel.scrollRectToVisible(view);
                            }
                        }
                    }

                    @Override
                    public void mouseMoved(MouseEvent e) {
                    }
                });
                newBoardButton.setBounds(xPosition, yPosition, BUTTON_SIZE, BUTTON_SIZE);
                newBoardButton.setIcon(currentTile.getImage());
                newBoardButton.addActionListener(this);
                newBoardButton.setBorder(null);

                //set any borders
                if (currentTile.hasOwner()) {
                    createLargerBorder(currentTile.getOwner().getColour(), newBoardButton);
                }
                yPosition = yPosition + BUTTON_SIZE; //increment y for next tile
                boardPanel.add(newBoardButton);
            }
            xPosition = xPosition + BUTTON_SIZE; //increment x for next tile
        }

        try {
            BufferedImage myPicture = ImageIO.read(new File("textures\\backgrounds\\menuBackground.jpg"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            boardPanel.add(picLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent arg0) {
        BoardButton button = (BoardButton) arg0.getSource();
        Coordinates buttonCoordinates = button.getCoordinates();

        Tile tileClicked = gameController.getMap().getTile(buttonCoordinates);

        resetUIColours();
        uiTextManager.updateInformationText(tileClicked);

        if (unitSelected) {
            if (currentCoordinates.x == buttonCoordinates.x && currentCoordinates.y == buttonCoordinates.y) {
                deselectUnit();
            } else {
                performUnitMovement(arg0, buttonCoordinates);
            }
        } else {
            performTileAction(buttonCoordinates, tileClicked);
        }
    }

    private void resetUIColours() {
        for (JButton UiButton : uiButtons) {
            UiButton.setBackground(Color.white);
            UiButton.setEnabled(true);
        }
    }

    private void deselectUnit() {
        unitSelected = false;
        updateLocalButtonIconsAndBorders();
        hideUIButtons();
    }

    private void performUnitMovement(ActionEvent arg0, Coordinates coordinates) {
        if (gameController.attackIsPossible(currentCoordinates, coordinates)) {
            gameController.performAttack(currentCoordinates, coordinates);
            updateLocalButtonIconsAndBorders();
            unitSelected = false;
        } else if (gameController.moveUnit(currentCoordinates, coordinates)) {
            boardButtons[currentCoordinates.x][currentCoordinates.y].setIcon(gameController.getTileImage(coordinates));
            unitSelected = false;
            updateLocalButtonIconsAndBorders();
            actionPerformed(arg0);
        }
    }

    private void performTileAction(Coordinates coordinates, Tile tileClicked) {
        currentCoordinates.setCoordinates(coordinates.x, coordinates.y);

        hideUIButtons();

        if (tileClicked.hasUnit()
                && tileClicked.getUnit().getOwner() == gameController.getCurrentPlayer()
                && tileClicked.getUnit().getRemainingMoves() > 0) {
            unitSelected = true;
            highlightTiles(tileClicked.getUnit().getRemainingMoves());
            setButtonText();
        } else if (tileClicked.hasBuilding() && tileClicked.getOwner() == gameController.getCurrentPlayer()) {
            setButtonText();
        }
    }

    private void highlightTiles(int maxUnitMoves) {
        final int BORDER_THICKNESS = 2;
        for (int endX = currentCoordinates.x - maxUnitMoves; endX <= currentCoordinates.x + maxUnitMoves; endX++) {
            for (int endY = currentCoordinates.y - maxUnitMoves; endY <= currentCoordinates.y + maxUnitMoves; endY++) {
                if (gameController.isValidMove(currentCoordinates, new Coordinates(endX, endY))) {
                    boardButtons[endX][endY].setBorder(BorderFactory.createLineBorder(Color.white, BORDER_THICKNESS));
                }
                if (gameController.attackIsPossible(currentCoordinates, new Coordinates(endX, endY))) {
                    boardButtons[endX][endY].setBorder(BorderFactory.createLineBorder(Color.red, BORDER_THICKNESS));
                }
            }
        }
    }
}

class FrameHandler {
    public static void createAndSetupFrameAndScrollPane(JPanel uiPanel, BoardPanel boardPanel) {
        JScrollPane scrollPane = new JScrollPane(boardPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JFrame frame = new JFrame("Civ like gameController");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane);
        frame.add(uiPanel, BorderLayout.SOUTH);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}