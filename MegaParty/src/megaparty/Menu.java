package megaparty;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Niklas
 */
public class Menu extends javax.swing.JFrame {

    private volatile MiniGame clickedMinigame;
    private volatile boolean miniGameClicked;
    private volatile boolean playClicked;

    private BufferedImage minigameIcon;
    private ArrayList<JButton> gameButtons;
    private MegaParty program;

    public Menu(MegaParty program) {
        initComponents();
        this.setTitle("Mega Party");
        this.program = program;
        gameButtons = new ArrayList();
        this.setVisible(true);
        this.setResizable(false);
    }

    public void update() {
        if (miniGameClicked && clickedMinigame != null) {
            miniGameClicked = false;
            clickedMinigame.init();
            minigameName.setText(clickedMinigame.getName());
            gameDescArea.setText(clickedMinigame.getDescription());
            minigameIcon = clickedMinigame.getImage();
            if(minigameIcon != null){
                gamePreviewImagePanel.getGraphics().drawImage(minigameIcon, 0, 0, gamePreviewImagePanel.getWidth(), gamePreviewImagePanel.getHeight(), null);
            }else{
                gamePreviewImagePanel.getGraphics().clearRect(0, 0, gamePreviewImagePanel.getWidth(), gamePreviewImagePanel.getHeight());
            }
        }
        if (playClicked && clickedMinigame != null) {
            program.setCurrentGame(clickedMinigame);
            program.setFrameVisiblity(true);
        }
        if(!program.getScoreBoard().isEmpty()){
            String sb = "";
            for(megaparty.Player p: program.getScoreBoard()){
                sb = sb + " " + p.getNickName() + "    " + p.getScore() + "\n";
            }
            playerScoreArea.setText(sb);
        }
    }

    public void addGameToList(MiniGame game) {
        gameButtons.add(new JButton());
        gameButtons.get(gameButtons.size() - 1).setFont(new java.awt.Font("Dialog", 1, 12));
        gameButtons.get(gameButtons.size() - 1).setText(game.getName());
        gameButtons.get(gameButtons.size() - 1).setSize(gameListPanel.getWidth() - 40, 40);
        gameButtons.get(gameButtons.size() - 1).setLocation(10, (gameButtons.size() - 1) * 50 + 5);
        gameButtons.get(gameButtons.size() - 1).addActionListener(new ActionListener() {
            private MiniGame ga = game;

            @Override
            public void actionPerformed(ActionEvent e) {
                clickedMinigame = ga;
                miniGameClicked = true;
            }
        });
        gameButtons.get(gameButtons.size() - 1).requestFocus();
    }

    public void refreshListPanel() {
        JPanel content = new JPanel();
        content.setLayout(null);
        for(JButton b: gameButtons){
            content.add(b);
        }
        content.setSize(gameListPanel.getWidth(), gameButtons.size() * 50 + 10);
        content.setPreferredSize(new java.awt.Dimension(gameListPanel.getWidth() - 40, gameButtons.size() * 50 + 10));
        gameListPanel.setViewportView(content);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gameListPanel = new javax.swing.JScrollPane();
        menuLabelName = new javax.swing.JLabel();
        gamePreviewImagePanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        playerScoreArea = new javax.swing.JTextArea();
        playButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        gameDescArea = new javax.swing.JTextArea();
        minigameName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        menuLabelName.setFont(new java.awt.Font("Dialog", 2, 48)); // NOI18N
        menuLabelName.setText("                  Mega Party");

        gamePreviewImagePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout gamePreviewImagePanelLayout = new javax.swing.GroupLayout(gamePreviewImagePanel);
        gamePreviewImagePanel.setLayout(gamePreviewImagePanelLayout);
        gamePreviewImagePanelLayout.setHorizontalGroup(
            gamePreviewImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 186, Short.MAX_VALUE)
        );
        gamePreviewImagePanelLayout.setVerticalGroup(
            gamePreviewImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        playerScoreArea.setColumns(20);
        playerScoreArea.setRows(5);
        playerScoreArea.setText("No Players Connected");
        playerScoreArea.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        playerScoreArea.setEnabled(false);
        jScrollPane2.setViewportView(playerScoreArea);

        playButton.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        playButton.setText("PLAY");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        gameDescArea.setColumns(20);
        gameDescArea.setRows(5);
        gameDescArea.setText("Select a minigame to the left.");
        gameDescArea.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        gameDescArea.setEnabled(false);
        jScrollPane3.setViewportView(gameDescArea);

        minigameName.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        minigameName.setText("No Minigame selected.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(menuLabelName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(gameListPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(minigameName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(gamePreviewImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuLabelName, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(gameListPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(gamePreviewImagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(playButton, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(minigameName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        if(clickedMinigame != null){
            playClicked = true;
        }
    }//GEN-LAST:event_playButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea gameDescArea;
    private javax.swing.JScrollPane gameListPanel;
    private javax.swing.JPanel gamePreviewImagePanel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel menuLabelName;
    private javax.swing.JLabel minigameName;
    private javax.swing.JButton playButton;
    private javax.swing.JTextArea playerScoreArea;
    // End of variables declaration//GEN-END:variables
}
