package com.example.funlearnv2.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.ItemChessBinding
import com.example.funlearnv2.utils.toGone
import com.example.funlearnv2.utils.toInvisible
import com.example.funlearnv2.utils.toVisible
import com.example.funlearnv2.utils.toast
import com.example.funlearnv2.views.fragments.*

class ItemChessAdapter : RecyclerView.Adapter<ItemChessAdapter.ViewHolder>() {
    
    private lateinit var context: Context
    private var mutableSelectedBox: MutableLiveData<ChessPiece> = MutableLiveData()
    val selectedBox: LiveData<ChessPiece> get() = mutableSelectedBox
    private var coordinateList = mutableListOf<ChessPiece>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ItemChessBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (GameFourFragment.chessPieceList.size==144){
            GameFourFragment.chessPieceList.clear()
        }
        fillData(position,holder.binding)
        if (coordinateList[position].type == ChessType.PLACEHOLDER){
            holder.binding.root.toGone()
        }
        else {
            if (coordinateList[position].box == ChessColor.WHITE) holder.binding.cardBoardPiece.setCardBackgroundColor(context.getColor(R.color.greyLight))
            else holder.binding.cardBoardPiece.setCardBackgroundColor(context.getColor(R.color.grey))
            if (coordinateList[position].type != ChessType.EMPTY){
                setImage(holder.binding.imagePiece,coordinateList[position].type)
                holder.binding.imagePiece.toVisible()
            }else holder.binding.imagePiece.toInvisible()
            holder.binding.root.setOnClickListener {
                val data = ChessPiece(
                    x = holder.binding.x.text.toString().toInt(),
                    y = holder.binding.y.text.toString().toInt(),
                    type = ChessType.valueOf(holder.binding.type.text.toString()),
                    box = ChessColor.valueOf(holder.binding.box.text.toString())
                )
                mutableSelectedBox.postValue(data)
            }
        }
        GameFourFragment.chessPieceList.add(holder.binding)

    }

    private fun fillData(position: Int,binding: ItemChessBinding){
        binding.x.text = coordinateList[position].x.toString()
        binding.y.text = coordinateList[position].y.toString()
        binding.type.text = coordinateList[position].type.toString()
        binding.box.text = coordinateList[position].box.toString()
    }

    private fun setImage(imageView: ImageView,type: ChessType){
        when(type){
            ChessType.BLACK_KING -> imageView.setImageResource(R.drawable.chess_king_black)
            ChessType.BLACK_QUEEN -> imageView.setImageResource(R.drawable.chess_queen_black)
            ChessType.BLACK_BISHOP -> imageView.setImageResource(R.drawable.chess_bishop_black)
            ChessType.BLACK_KNIGHT -> imageView.setImageResource(R.drawable.chess_horse_black)
            ChessType.BLACK_ROOK -> imageView.setImageResource(R.drawable.chess_rook_black)
            ChessType.BLACK_PAWN,ChessType.BLACK_PAWN_FIRST -> imageView.setImageResource(R.drawable.chess_pawn_black)
            ChessType.WHITE_KING -> imageView.setImageResource(R.drawable.chess_king_white)
            ChessType.WHITE_QUEEN -> imageView.setImageResource(R.drawable.chess_queen_white)
            ChessType.WHITE_BISHOP -> imageView.setImageResource(R.drawable.chess_bishop_white)
            ChessType.WHITE_KNIGHT -> imageView.setImageResource(R.drawable.chess_horse_white)
            ChessType.WHITE_ROOK -> imageView.setImageResource(R.drawable.chess_rook_white)
            ChessType.WHITE_PAWN,ChessType.WHITE_PAWN_FIRST -> imageView.setImageResource(R.drawable.chess_pawn_white)
            else -> imageView.setImageResource(R.drawable.placeholder_icon)
        }
    }

    fun updateList(list: MutableList<ChessPiece>){
        coordinateList.clear()
        coordinateList.addAll(list)
        notifyDataSetChanged()
    }
    
    override fun getItemCount(): Int = coordinateList.size

    override fun getItemViewType(position: Int): Int = position

    class ViewHolder(val binding: ItemChessBinding) : RecyclerView.ViewHolder(binding.root)
}