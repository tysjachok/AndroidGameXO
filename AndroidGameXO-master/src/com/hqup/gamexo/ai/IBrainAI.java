package kn.hqup.gamexo.ai;

/**
 * Created with IntelliJ IDEA.
 * User: Andrew2212
 * <br>Brain of the 'playerBot'. Its one-and-only method get current 'fieldMatrix' and return the 'best move'</br>
 * <br></br>
 */
public interface IBrainAI<T>{
      int[] findMove(T[][] fieldMatrix, T figure);
}
