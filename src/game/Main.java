package game;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        JFrame game = new JFrame();

        game.setTitle("2048");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(450, 500);
        game.setResizable(false);

        game.add(controller.getView());

        Integer i;

        game.setLocationRelativeTo(null);
        game.setVisible(true);


        // Model model = new Model();
//        Model m = new Model();
////        m.gameTiles = new Tile[][]{{new Tile(4), new Tile(4), new Tile(2), new Tile(0)},
////                {new Tile(4), new Tile(2), new Tile(0), new Tile(4)},
////                {new Tile(4), new Tile(4), new Tile(4), new Tile(0)},
////                {new Tile(4), new Tile(4), new Tile(4), new Tile(4)}};
//
//        m.gameTiles = new Tile[][]{{new Tile(4), new Tile(4), new Tile(2), new Tile(0)},
//                {new Tile(4), new Tile(2), new Tile(0), new Tile(4)},
//                {new Tile(4), new Tile(4), new Tile(4), new Tile(0)},
//                {new Tile(4), new Tile(4), new Tile(4), new Tile(4)}};
//
//
//        System.out.println( m.canMove());
//
//
//        for (Tile[] gameTile : m.gameTiles) {
//            for (Tile tile : gameTile) {
//                System.out.print(tile);
//            }
//            System.out.println();
//        }
//
//        m.rotateCW();
//        System.out.println();
//        m.rotateCW();
//        System.out.println();
//
//
//        for (Tile[] gameTile : m.gameTiles) {
//            for (Tile tile : gameTile) {
//                System.out.print(tile);
//            }
//            System.out.println();
        // }
//
//        m.mergeTiles(m.gameTiles[0]);
//        m.mergeTiles(m.gameTiles[1]);
//        m.mergeTiles(m.gameTiles[2]);
//        m.mergeTiles(m.gameTiles[3]);


    }
}
