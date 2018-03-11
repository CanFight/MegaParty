package megaparty;

import javax.swing.JFrame;

public class GameFrame extends JFrame{

    //private InputHandler inputHandler;
    private int windowWidth, windowHeight;
    private MegaParty program;
    
    public GameFrame(MegaParty prog){
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setTitle("[Mega Party]");
        windowWidth = this.getWidth();
        windowHeight = this.getHeight();
        program = prog;
        
        java.awt.Dimension objDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int iCoordX = (objDimension.width - this.getWidth()) / 2;
        int iCoordY = (objDimension.height - this.getHeight()) / 2;
        this.setLocation(iCoordX, iCoordY); 
        
        //inputHandler = new InputHandler(prog);
        //addKeyListener(inputHandler);
        //addMouseListener(inputHandler);
        //addMouseMotionListener(inputHandler);
    }

    //public InputHandler getInputHandler() {
    //    return inputHandler;
    //}
    
}
