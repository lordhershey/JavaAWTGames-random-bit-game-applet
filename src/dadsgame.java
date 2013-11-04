import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.*;
import java.net.*;
import java.awt.Graphics2D;
import java.util.logging.*;
import javax.sound.sampled.AudioFormat;
import java.util.*;
import java.awt.geom.*;

/*JRTNJ4vines*/

/*
 
0,    1/2,  2/5,  2/5,  1/8,  5/12,  3/10,   1/2
1/2,  0,    2/3,  2/3,  1/4,  5/8,   1/2,    7/10
3/5,  1/3,  0,    1/2,  1/2,  1/2,   3/8,    7/12
3/5,  1/3,  1/2,  0,    1/2,  1/2,   3/4,    7/8
7/8,  3/4,  1/2,  1/2,  0,    1/2,   1/3,    3/5
7/12, 3/8,  1/2,  1/2,  1/2,  0,     1/3,    3/5 
7/10, 1/2,  5/8,  1/4,  2/3,  2/3,   0,      1/2
1/2,  3/10, 5/12, 1/8,  2/5,  2/5,   1/2,    0


tripple b row tripple column probability that triple b happenes before tripple a
*/

class toggleActionListener implements ActionListener
{
	dadsgame l_dg = null;
	int l_idx = -1;

	public toggleActionListener(dadsgame p_d,int p_idx)
	{
		l_dg = p_d;
		l_idx = p_idx;
	}

	public void actionPerformed(ActionEvent e)
	{
		l_dg.playerBet[l_idx] = !l_dg.playerBet[l_idx];
		//l_dg.drawGameScreen();
		l_dg.repaint(0);
	}
}

class startActionListener implements ActionListener
{

	Button l_button = null;
	dadsgame l_dg = null;

	public startActionListener(Button p_b, dadsgame p_d)
	{
		l_button = p_b;
		l_dg = p_d;
	}


	public void actionPerformed(ActionEvent e)
	{
		if(l_dg.playing)
		    {
			l_dg.playing = false;
			l_dg.showComputerBet = false;
			l_dg.showComputerBet = false;
			//l_dg.repaint(0);
			//l_dg.repaint(0);
		    }
		else if (l_dg.needReset)
		    {
			l_button.setLabel(" Play ");
			l_dg.needReset = false;;
			l_dg.playing = false;
			l_dg.showComputerBet = false;
			l_dg.computerWon = false;
			l_dg.playerWon = false;
			l_dg.toggle1.setEnabled(true);
			l_dg.toggle2.setEnabled(true);
			l_dg.toggle3.setEnabled(true);
		    }
		else
		    {
			l_dg.playing = true;
			l_dg.showComputerBet = true;
			l_button.setLabel("Reset");
			//l_dg.repaint(0);
		    }
	}
}


public final class dadsgame 
    extends Applet implements Runnable
{

    public static Image wNumbers[] = null;
    public static Image midGround = null;
    public static Image db = null;
    Graphics superg = null;
    
    public static boolean playerBet[] = null;
    public static boolean computerBet[] = null;
    public static boolean showComputerBet = false;
    
    public static Image redSquare = null;
    public static Image blackSquare = null;
    public static Image greenSquare = null;
    
    public static Image overPlatePattern = null;
    public static Image underPlatePattern = null;
    
    public static Image houseBetImage = null;
    public static Image playerBetImage = null;
    
    public static Image winnerImage = null;
    
    public static boolean playing = false;
    public static boolean needReset = false;    

    public static int backwardX = 0;
    public static int wheelIDX = 0;
    
    public boolean computerWon = false;
    public boolean playerWon = false;
    
    Button toggle1 = null;
    Button toggle2 = null;
    Button toggle3 = null;
    Button Start = null;
    
    public int resultScrollX = 0;
    public int scrollSquares[] = null;

    public dadsgame(){
	//empty public constructor
    }

    public void init(){
	playing = false;
	wNumbers = new Image[38];
	db = createImage(575,476);
	setSize(575,505); // playfield is 24w x 16h 30pixel squares
	setLayout(new BorderLayout());	// set layout type for the playfield
	
	Start=new Button(" Play ");
	
	scrollSquares = new int[5];
	for(int i = 0 ; i < 5 ; i++)
	    {
		scrollSquares[i] = 0;
	    }
	
	toggle1 = new Button("Toggle 1");
	toggle2 = new Button("Toggle 2");
	toggle3 = new Button("Toggle 3");
	
	playerBet = new boolean[3];
	computerBet = new boolean[3];
	
	for( int i = 0 ; i < 3 ; i++)
	    {
		playerBet[i] = false;
		computerBet[i] = false;
	    }
	


	Panel southPanel = new Panel();
	southPanel.setLayout(new BorderLayout());

	add(southPanel, BorderLayout.SOUTH);

	Panel ButtonPanel = new Panel();
	ButtonPanel.setLayout(new FlowLayout());
	ButtonPanel.setBackground(null);

	//add(ButtonPanel, BorderLayout.SOUTH);
	southPanel.add(ButtonPanel, BorderLayout.NORTH);

	ButtonPanel.add(Start);
	ButtonPanel.add(toggle1);
	ButtonPanel.add(toggle2);
	ButtonPanel.add(toggle3);

	Panel copyRightPanel = new Panel();
	copyRightPanel.setLayout(new FlowLayout());
	copyRightPanel.add(new Label("Copyright 2009 John Hershey"));

	southPanel.add(copyRightPanel, 
		       BorderLayout.SOUTH);

	toggle1.addActionListener( new toggleActionListener(this,0) );
	toggle2.addActionListener( new toggleActionListener(this,1) );
	toggle3.addActionListener( new toggleActionListener(this,2) );
	Start.addActionListener( new startActionListener(Start,this));
    }

    public void start(){
	
	repaint(0);
	int i = 0;
	try{
	    redSquare = getImage(getCodeBase(),"images/RedSquare.gif");
	    blackSquare = getImage(getCodeBase(),"images/BlackSquare.gif");
	    greenSquare = getImage(getCodeBase(),"images/GreenSquare.gif");
	    overPlatePattern = getImage(getCodeBase(),"images/OverPlatePattern.gif");
	    underPlatePattern = getImage(getCodeBase(),"images/UnderPlatePattern.gif");
	    
	    playerBetImage = getImage(getCodeBase(),"images/PlayerBet.gif");
	    houseBetImage = getImage(getCodeBase(),"images/HouseBet.gif");
	    winnerImage = getImage(getCodeBase(),"images/Winner.gif");
	    
	    midGround = getImage(getCodeBase(),"images/MidGround.gif");
	    i = 0;
	    wNumbers[i++] = getImage(getCodeBase(),"images/g_00.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_1.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_13.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_36.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_24.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_3.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_15.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_34.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_22.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_5.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_17.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_32.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_20.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_7.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_11.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_30.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_26.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_9.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_28.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/g_0.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_2.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_14.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_35.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_23.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_4.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_16.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_33.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_21.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_6.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_18.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_31.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_19.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_8.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_12.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_29.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_25.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/b_10.gif");
	    wNumbers[i++] = getImage(getCodeBase(),"images/r_27.gif");
	}
	catch(Exception e){
	    System.out.println("Got to use tool kit " + e.getMessage());
	    
	    Toolkit t = Toolkit.getDefaultToolkit();
	    
	    redSquare = t.createImage(getClass().getResource("image/RedSquare.gif"));
	    blackSquare = t.createImage(getClass().getResource("image/BlackSquare.gif"));
	    greenSquare = t.createImage(getClass().getResource("image/GreenSquare.gif"));
	    overPlatePattern = t.createImage(getClass().getResource("image/OverPlatePattern.gif"));
	    underPlatePattern = t.createImage(getClass().getResource("image/UnderPlatePattern.gif"));
	    winnerImage = t.createImage(getClass().getResource("image/Winner.gif"));
	    
	    playerBetImage = t.createImage(getClass().getResource("image/PlayerBet.gif"));
	    houseBetImage = t.createImage(getClass().getResource("image/HouseBet.gif"));
	    
	    midGround = t.createImage(getClass().getResource("image/MidGround.gif"));
	    i = 0;
	    wNumbers[i++] = t.createImage(getClass().getResource("image/g_00.gif")); /*0*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_1.gif"));  /*1*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_13.gif")); /*2*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_36.gif")); /*3*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_24.gif")); /*4*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_3.gif"));  /*5*/ 
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_15.gif")); /*6*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_34.gif")); /*7*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_22.gif")); /*8*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_5.gif"));  /*9*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_17.gif")); /*10*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_32.gif")); /*11*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_20.gif")); /*12*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_7.gif"));  /*13*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_11.gif")); /*14*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_30.gif")); /*15*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_26.gif")); /*16*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_9.gif"));  /*17*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_28.gif")); /*18*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/g_0.gif"));  /*19*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_2.gif"));  /*20*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_14.gif")); /*21*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_35.gif")); /*22*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_23.gif")); /*23*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_4.gif"));  /*24*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_16.gif")); /*25*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_33.gif")); /*26*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_21.gif")); /*27*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_6.gif"));  /*28*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_18.gif")); /*29*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_31.gif")); /*30*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_19.gif")); /*31*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_8.gif"));  /*32*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_12.gif")); /*33*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_29.gif")); /*34*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_25.gif")); /*35*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/b_10.gif")); /*36*/
	    wNumbers[i++] = t.createImage(getClass().getResource("image/r_27.gif")); /*37*/
	}
	
	System.out.println("The length of this thing " + i);
	
	MediaTracker MT;
	MT = new MediaTracker(this);
	
	for(i = 0 ; i < wNumbers.length ; i++)
	    {
		MT.addImage(wNumbers[i],0);
	    }
	MT.addImage(midGround,0);
	MT.addImage(redSquare,0);
	MT.addImage(blackSquare,0);
	MT.addImage(greenSquare,0);
	MT.addImage(underPlatePattern,0);
	MT.addImage(overPlatePattern,0);
	
	MT.addImage(playerBetImage,0);
	MT.addImage(houseBetImage,0);
	MT.addImage(winnerImage,0);
	
	try{MT.waitForAll();}
	catch(Exception e){}
	
	System.out.println("Got the Goods");
	
	System.out.println("About to Start!");
	
	repaint(0);
	paintAll(superg);

	Thread T = new Thread(this);
	T.start();
    }
    
    public void fastPaint()
    {
	Graphics g = getGraphics();
	if(g == null || db == null)
	    return;
	
	g.drawImage(db,0,0,null);
    }
    
    
    public void update(Graphics g)
    {
	paint(g);
    }
    
    
    public void paint(Graphics g)
    {
	super.paint(g);
	while(superg == null)
	    {
		superg = getGraphics();
		try
		    {
			Thread.sleep(50);
		    }
		catch(Exception e)
		    {
		    }
		
		repaint(0);
		update(superg);
	    }
	drawGameScreen();
	fastPaint();
	toggle1.paint(g);
	toggle2.paint(g);
	toggle3.paint(g);
	Start.paint(g);
    }
    
    
    public void run()
    {

	while(superg == null)
	    {
		superg = getGraphics();
		try
		    {
			Thread.sleep(50);
		    }
		catch(Exception e)
		    {
		    }
		
		repaint(0);
		update(superg);
	    }
	
	while(true)
	    {
		if(needReset)
		    {
			drawGameScreen();
			fastPaint();
		    }
		else if(!playing)
		    {
			
			for(int i = 0 ; i < 5 ; i++)
		    {
			scrollSquares[i] = 0;
		    }
			
			drawGameScreen();
			fastPaint();
		    }
		else
		    {
			playGame();
			playing = false;
			needReset = true;
		    }
	       
		try{Thread.sleep(100);}
		catch(Exception e){}
	    }

    }
    
    
    public void playGame()
    {
	int ret = 0;
	int compBet[] = new int[3];
	int playBet[] = new int[3];
	
	toggle1.setEnabled(false);
	toggle2.setEnabled(false);
	toggle3.setEnabled(false);
	Start.setEnabled(false);
	
	int pbet = 0;
	
	if(playerBet[0])
	    {
		pbet += 4;
	    }
	
	if(playerBet[1])
	    {
		pbet += 2;
	    }
	
	if(playerBet[2])
	    {
		pbet += 1;
	    }
	
	switch(pbet)
	    {
	    case 0:
	    default:
		computerBet[2] = false;
		computerBet[1] = false;
		computerBet[0] = true;
		break;
		
	    case 1:
		computerBet[2] = false;
		computerBet[1] = false;
		computerBet[0] = true;
		break;
		
	    case 2:
		computerBet[2] = true;
		computerBet[1] = false;
		computerBet[0] = false;
		break;
		
	    case 3:
		computerBet[2] = true;
		computerBet[1] = false;
		computerBet[0] = false;
		break;
		
	    case 4:
		computerBet[2] = false;
		computerBet[1] = true;
		computerBet[0] = true;
		break;
		
	    case 5:
		computerBet[2] = false;
		computerBet[1] = true;
		computerBet[0] = true;
		break;
		
	    case 6:
		computerBet[2] = true;
		computerBet[1] = true;
		computerBet[0] = false;
		break;
		
	    case 7:
		computerBet[2] = true;
		computerBet[1] = true;
		computerBet[0] = false;
		break;
		
	    }
	
	computerWon = false;
	playerWon = false;
	
	for(int i = 0; i < 3 ; i++)
	    {
		compBet[i] = 1;
		playBet[i] = 1;
		if(playerBet[i])
		    playBet[i] = 2;
		
		if(computerBet[i])
		    compBet[i] = 2;
	    }
	
	for( ; ; )
	    {
		ret = spinTheWheel();
		switch(ret)
		    {
		    case 0:
		    default:
			System.out.println("Green");
			break;
		    case 1:
			System.out.println("Black");
			scrollResult(1);
			break;
		    case 2:
			System.out.println("Red");
			scrollResult(2);
			break;
		    }
		try{Thread.sleep(250);}
		catch(Exception e){}
		if( scrollSquares[0] == compBet[0] &&
		    scrollSquares[1] == compBet[1] &&	
		    scrollSquares[2] == compBet[2] )
		    {
			System.out.println("Computer Wins");
			computerWon = true;
			break;
		    }
		
		if( scrollSquares[0] == playBet[0] &&
		    scrollSquares[1] == playBet[1] &&	
		    scrollSquares[2] == playBet[2] )
		    {
			System.out.println("Player Wins");
			playerWon = true;
			break;
		    }
		
	    }
	System.out.println("End of the Game");
	Start.setEnabled(true);
    }
    
    public int spinTheWheel()
    {
	int chx = -70 - (int)(Math.random() * 75);
        boolean hasstoped = false;	
	boolean stopthewheel = false;
	for(;chx != 0;)
	    {
		backwardX += chx;
		chx = (int)((double)chx * 0.98);
		
		if(chx > -1 && backwardX != -48 && backwardX <= -24)
		    {
			stopthewheel = true;
			chx = -1;
			//System.out.println("Adjusting Backward X " + backwardX + " " + chx);
		    }
		else if(chx > -1 && backwardX < 0)
		    {
			stopthewheel = true;
			chx = 1;
		    }
		
		while(backwardX <= -48)
		    {
			wheelIDX++;
			wheelIDX = wheelIDX % 38;
			backwardX += 48;
		    }
		
		drawGameScreen();
		fastPaint();
		if(stopthewheel && !hasstoped)
		    {
			hasstoped = true;
			try{Thread.sleep(150);}
			catch(Exception e){}
		    }
		try{Thread.sleep(50);}
		catch(Exception e){}
	    }
	
	//System.out.println("Backward X is " + backwardX);
	
	return( getTheResultColor() );
    }
    
    public void scrollResult(int p_val)
    {
	scrollSquares[3] = p_val;
	
	for(resultScrollX = 0 ; resultScrollX < 38 ; resultScrollX++)
	    {
		drawGameScreen();
		fastPaint();
		try{Thread.sleep(10);}
		catch(Exception e){}
	    }
	
	resultScrollX = 0;
	
	scrollSquares [0] = scrollSquares [1];
	scrollSquares [1] = scrollSquares [2];
	scrollSquares [2] = scrollSquares [3];
	
    }
    
    int getTheResultColor()
    {
	int l_idx = (wheelIDX + 5) % 38;
	switch(l_idx)
	    {
	    case 2:
	    case 4:
	    case 6:
	    case 8:
	    case 10:
	    case 12:
	    case 14:
	    case 16:
	    case 18:
	    case 20:
	    case 22:
	    case 24:
	    case 26:
	    case 28:
	    case 30:
	    case 32:
	    case 34:
	    case 36:
		return(1); /*black*/
		
	    case 1:
	    case 3:
	    case 5:
	    case 7:
	    case 9:
	    case 11:
	    case 13:
	    case 15:
	    case 17:
	    case 21:
	    case 23:
	    case 25:
	    case 27:
	    case 29:
	    case 31:
	    case 33:
	    case 35:
	    case 37:
		return(2); /*red*/
		
	    case 0:
	    case 19: /* green */
	    default:
		return(0); /*green*/
	    }
    }
    
    public void drawComputerBet(Graphics g, int x, int y)
    {
	int offx1 = x + 44;
	int offx2 = x + 44 + 37;
	int offx3 = x + 44 + 37 + 37;
	int offy = y + 2;
	
	g.drawImage(underPlatePattern,x,y,null);
	
	if( !showComputerBet )
	    {
		g.drawImage(greenSquare,offx1,offy,null);
		g.drawImage(greenSquare,offx2,offy,null);
		g.drawImage(greenSquare,offx3,offy,null);
	    }
	else
	    {
		if( computerBet[0] )
		    {
			g.drawImage(redSquare,offx1,offy,null);
		    }
		else
		    {
			g.drawImage(blackSquare,offx1,offy,null);
		    }
		
		if( computerBet[1] )
		    {
			g.drawImage(redSquare,offx2,offy,null);
		    }
		else
		    {
			g.drawImage(blackSquare,offx2,offy,null);
		    }
		
		if( computerBet[2] )
		    {
			g.drawImage(redSquare,offx3,offy,null);
		    }
		else
		    {
			g.drawImage(blackSquare,offx3,offy,null);
		    }
		
	    }
	
	g.drawImage(overPlatePattern,x,y,null);
    }
    
    public void drawPlayerBet(Graphics g, int x, int y)
    {
	int offx1 = x + 44;
	int offx2 = x + 44 + 37;
	int offx3 = x + 44 + 37 + 37;
	int offy = y + 2;
	
	g.drawImage(underPlatePattern,x,y,null);
	
	if( playerBet[0] )
	    {
		g.drawImage(redSquare,offx1,offy,null);
	    }
	else
	    {
		g.drawImage(blackSquare,offx1,offy,null);
	    }
	
	if( playerBet[1] )
	    {
		g.drawImage(redSquare,offx2,offy,null);
	    }
	else
	    {
		g.drawImage(blackSquare,offx2,offy,null);
	    }
	
	if( playerBet[2] )
	    {
		g.drawImage(redSquare,offx3,offy,null);
	    }
	else
	    {
		g.drawImage(blackSquare,offx3,offy,null);
	    }
	
	g.drawImage(overPlatePattern,x,y,null);
    }
    
    public void drawSliderBox(Graphics g, int x, int y)
    {
	int i;
	int startx;
	int offy = 2;
	int offx = 44;
	
	g.drawImage(underPlatePattern,x,y,null);
	
	startx = (x + offx) - resultScrollX;
	for(i = 0 ; i < 4 ; i++, startx += 37)
	    {
		switch(scrollSquares[i])
		    {
		    case 0:
		    default:
			g.drawImage(greenSquare,startx,y + offy,null);
			break;
		    case 1:
			g.drawImage(blackSquare,startx,y + offy,null);
			break;
		    case 2:
			g.drawImage(redSquare,startx,y + offy,null);
			break;
			
		    }
	    }
	
	g.drawImage(overPlatePattern,x,y,null);
    }
    
    public void drawGameScreen()
    {
	int lowerb = 0;
	int i;
	
	int startx = 18 + backwardX;
	Graphics g = null;
	
	g = db.getGraphics();
	
	if (backwardX > -1) 
	    {
		lowerb = -1;
		startx = startx - 49;
	    }
	
	for(i = lowerb ; i < 12 ; i ++ , startx += 49)
	    {
		int y = 29;
		int theIDX = (wheelIDX + i + 38) % 38;
		
		if(i < 2 || i >8)
		    {
			y -= 3;
		    }
		else if(i < 4 || i > 6)
		    {
			y -= 1;
		    }
		
		
		g.drawImage(wNumbers[theIDX],startx,y,null);
		//System.out.println("wheel IDX " + theIDX + " , " + startx);
	    }
	
	g.drawImage(midGround,0,0,null);
	
	g.drawImage(houseBetImage,38,117,null);
	drawComputerBet(g, 192, 113);
	
	if(computerWon)
	    {
		g.drawImage(winnerImage,399,117,null);
	    }
	
	drawSliderBox(g, 192, 153);
	
	g.drawImage(playerBetImage,38,195,null);
	drawPlayerBet(g, 192, 193);
	
	if(playerWon)
	    {
		g.drawImage(winnerImage,399,197,null);
	    }
	
    }
    
    public static void main(String args[]){
	
	String D=new String("Welcome To Dad's Game!");
	
	System.out.println(D);
	
	dadsgame P = new dadsgame();
	
	Frame f = new Frame(D);
	Dimension dim = new Dimension((575+10),(505+30));
	f.setSize(dim);
	
	f.add("Center",P);
	f.setVisible(true);
	
	f.addWindowListener(new WindowListener()
	    {
		public void windowActivated(WindowEvent e){} 
		public void windowClosed(WindowEvent e){}
		public void windowClosing(WindowEvent e)
		{
		    System.exit(0);
		}
		public void windowDeactivated(WindowEvent e){}
		public void windowDeiconified(WindowEvent e){}
		public void windowIconified(WindowEvent e){}
		public void windowOpened(WindowEvent e){}
	    });
	
	P.init();
	P.start();
	
    }
    
}
