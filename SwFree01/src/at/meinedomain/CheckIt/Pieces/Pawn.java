package at.meinedomain.CheckIt.Pieces;

import android.util.Log;
import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.MoveType;
import at.meinedomain.CheckIt.Point;

public class Pawn extends AbstractPiece {
	
	protected int direction;

	public Pawn(Board b, Color c, Point pt){
		super(b, c, pt, "PAWN");
		if(c == Color.WHITE){
			pixmap = Assets.wp;
			direction = 1;
		} else{
			pixmap = Assets.bp;
			direction = -1;
		}
	}
	
	@Override
	protected MoveType canMove(Point to) {
		// TODO first test if the move would leave us in check e.g. by 
		//      looking we are in check on a board where this piece is missing!
		
		// - go 1 step ahead
		if(isEmpty(to) && isOnSameFile(to) && verticalDiff(to)==direction){
			return MoveType.NORMAL;
			// TODO let Pawn turn into a queen or something other, when last rank is reached
		}
		
		// - capture piece
		if(horizontalDist(to) == 1 && verticalDiff(to) == direction){
			if(isOccupiedByOpponent(to)){
				return MoveType.NORMAL;
			}
			if(isEnPassant(to)){
				return MoveType.EN_PASSANT;
			}
		}

		// - go 2 steps ahead
		if(isEmpty(to) && 
		   isOnSameFile(to) && 
		   verticalDiff(to)==2*direction &&
		   isOnBaseline() && 
		   isEmpty(location.getX(), location.getY()+direction)){
			
			return MoveType.DOUBLE_STEP;	// TODO set EN_PASSANT-Point on the board DO THIS IN MOVE TO GET THE EN_PASSANT POSSIBILIETIES AFTER OPPONENTS TURN!!!
											// TODO set flag, that opponent gets notified of en-passant-possibility
		}

		return MoveType.ILLEGAL;
		
	}

	@Override
	public MoveType tryToMove(Point to) {
		// TODO the normal checks + check for en passant (see tryToMove() in Piece in the DD)
		MoveType mt = super.tryToMove(to);
		if(mt == MoveType.ILLEGAL  ||  mt == MoveType.NORMAL){
			return mt;
		}
		else if(mt == MoveType.DOUBLE_STEP){
			board.move(location, to, new Point(location.getX(), 
											   location.getY()+direction));
		}
		else if(mt == MoveType.EN_PASSANT){
			board.move(location, to);
			// TODO remove opponent's pawn
		}
		return mt;
	}
	
	private boolean isOnBaseline(){
		if(color == Color.WHITE){
			return (location.getY() == 1);
		}
		else{
			return (location.getY() == board.getHeight()-2);
		}
	}

}
