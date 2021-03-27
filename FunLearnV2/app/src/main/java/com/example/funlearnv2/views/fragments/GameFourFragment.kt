package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.FragmentGameFourBinding
import com.example.funlearnv2.databinding.ItemChessBinding
import com.example.funlearnv2.utils.toGone
import com.example.funlearnv2.utils.toVisible
import com.example.funlearnv2.views.adapters.ItemChessAdapter
import com.example.funlearnv2.views.adapters.ItemDeadChessAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class GameFourFragment : Fragment() {

    private var _binding: FragmentGameFourBinding? = null
    private val binding
        get() = _binding!!

    private val itemDeadBlackChessAdapter = ItemDeadChessAdapter()
    private val itemChessAdapter = ItemChessAdapter()
    private val itemDeadWhiteChessAdapter = ItemDeadChessAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameFourBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        binding.recyclerChessDeadBlack.apply {
            adapter = itemDeadBlackChessAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context,8)
        }

        binding.recyclerChessBoard.apply {
            adapter = itemChessAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context,12)
        }

        itemChessAdapter.updateList(root.coordinateList)

        binding.recyclerChessDeadWhite.apply {
            adapter = itemDeadWhiteChessAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context,8)
        }

        observerClick()
    }

    private fun observerClick(){
        itemChessAdapter.selectedBox.removeObservers(viewLifecycleOwner)
        itemChessAdapter.selectedBox.observe(viewLifecycleOwner,{
            executePlayerMove(it)
        })
    }

    private fun executePlayerMove(chessPiece: ChessPiece?) {
        if (chessPiece!= null && root.currentPlayer == ChessColor.WHITE){
            if (locationList.isNotEmpty()){
                if (verifyLocation(locationList,chessPiece.x,chessPiece.y)) movePiece(currentPiece,chessPiece)

            }else{
                currentPiece = chessPiece.copy()
                makeMove(chessPiece.x,chessPiece.y,chessPiece.type)
            }
        }
    }

    private fun insertTree(x1:Int,y1:Int,x2: Int,y2: Int){
        val next = root.copy()

        next.coordinateList[(x2*12)+y2].type = next.coordinateList[(x1*12)+y1].type
        next.coordinateList[(x1*12)+y1].type = ChessType.EMPTY

        if(!isRepeat(next)){
            next.materials.putAll(root.materials)
            if(next.coordinateList[(x2*12)+y2].type != ChessType.EMPTY)
                next.materials[next.coordinateList[(x2*12)+y2].type]!!.minus(1)

            next.whiteKingPos = root.whiteKingPos.copy()
            next.blackKingPos = root.blackKingPos.copy()
            if (root.currentPlayer == ChessColor.WHITE) next.currentPlayer = ChessColor.BLACK
            else next.currentPlayer = ChessColor.WHITE
            root.next.add(next)
        }
    }

    private fun deleteTree(root: Node){
        if (root == null) return
        if(root.next.isEmpty()) return

        for(item in root.next){
            deleteTree(item)
            root.next.removeLast()
        }
    }

    private fun isSameSide(side:ChessColor,target:ChessType):Boolean {
        return when(target){
            ChessType.BLACK_KING,ChessType.BLACK_QUEEN,ChessType.BLACK_BISHOP,ChessType.BLACK_KNIGHT,ChessType.BLACK_ROOK,ChessType.BLACK_PAWN,ChessType.BLACK_PAWN_FIRST ->{
                side == ChessColor.BLACK
            }
            ChessType.WHITE_KING,ChessType.WHITE_QUEEN,ChessType.WHITE_BISHOP,ChessType.WHITE_KNIGHT,ChessType.WHITE_ROOK,ChessType.WHITE_PAWN,ChessType.WHITE_PAWN_FIRST ->{
                side == ChessColor.WHITE
            }
            else -> false
        }
    }

    private fun nextMove(depth: Int) {
        if(depth >= MAX_DEPTH) return

        for (i in 2..9){
            for (j in 2..9){
                var c = root.coordinateList[(i*12)+j].type
                if (root.currentPlayer == ChessColor.WHITE){
                    when (c){
                        ChessType.BLACK_QUEEN -> moveQueen(i,j,ChessColor.BLACK)
                        ChessType.BLACK_ROOK -> moveRook(i,j,ChessColor.BLACK)
                        ChessType.BLACK_BISHOP -> moveBishop(i,j,ChessColor.BLACK)
                        ChessType.BLACK_KNIGHT -> moveKnight(i,j,ChessColor.BLACK)
                        ChessType.BLACK_PAWN_FIRST -> movePawn(i,j,ChessColor.BLACK)
                        ChessType.BLACK_PAWN -> movePawn(i,j,ChessColor.BLACK)
                        ChessType.BLACK_KING -> moveKing(i,j,ChessColor.BLACK)
                        else -> return
                    }
                }else{
                     when (c){
                        ChessType.WHITE_QUEEN -> moveQueen(i,j,ChessColor.WHITE)
                        ChessType.WHITE_ROOK -> moveRook(i,j,ChessColor.WHITE)
                        ChessType.WHITE_BISHOP -> moveBishop(i,j,ChessColor.WHITE)
                        ChessType.WHITE_KNIGHT -> moveKnight(i,j,ChessColor.WHITE)
                        ChessType.WHITE_PAWN_FIRST -> movePawn(i,j,ChessColor.WHITE)
                        ChessType.WHITE_PAWN -> movePawn(i,j,ChessColor.WHITE)
                        ChessType.WHITE_KING -> moveKing(i,j,ChessColor.WHITE)
                        else -> return
                    }
                }
            }
        }

        for (item in root.next) nextMove(depth+1)
    }

    private fun materialScore(m:MutableMap<ChessType,Int>):Int{
        val black = WEIGHT_PAWN* m[ChessType.BLACK_PAWN]!! + WEIGHT_KNIGHT*m[ChessType.BLACK_KNIGHT]!! + WEIGHT_BISHOP*m[ChessType.BLACK_BISHOP]!! + WEIGHT_ROOK*m[ChessType.BLACK_ROOK]!! + WEIGHT_QUEEN*m[ChessType.BLACK_QUEEN]!! + WEIGHT_KING*m[ChessType.BLACK_KING]!!
        val white = WEIGHT_PAWN* m[ChessType.WHITE_PAWN]!! + WEIGHT_KNIGHT*m[ChessType.WHITE_KNIGHT]!! + WEIGHT_BISHOP*m[ChessType.WHITE_BISHOP]!! + WEIGHT_ROOK*m[ChessType.WHITE_ROOK]!! + WEIGHT_QUEEN*m[ChessType.WHITE_QUEEN]!! + WEIGHT_KING*m[ChessType.WHITE_KING]!!
        return (white - black)
    }

    private fun evaluate(root: Node):Double{
        val score = materialScore(root.materials)
        for (i in 2..9){
            for (j in 2..9){
                val c = root.coordinateList[(i*12)+j].type

                if (c == ChessType.WHITE_KING) score.and(WHITE_KING[(i-2)*(j-2)])
                if (c == ChessType.WHITE_QUEEN) score.and(WHITE_QUEEN[(i-2)*(j-2)])
                if (c == ChessType.WHITE_ROOK) score.and(WHITE_ROOK[(i-2)*(j-2)])
                if (c == ChessType.WHITE_BISHOP) score.and(WHITE_BISHOP[(i-2)*(j-2)])
                if (c == ChessType.WHITE_KNIGHT) score.and(WHITE_KNIGHT[(i-2)*(j-2)])
                if (c == ChessType.WHITE_PAWN) score.and(WHITE_PAWN[(i-2)*(j-2)])

                if (c == ChessType.BLACK_KING) score.and(BLACK_KING[(i-2)*(j-2)])
                if (c == ChessType.BLACK_QUEEN) score.and(BLACK_QUEEN[(i-2)*(j-2)])
                if (c == ChessType.BLACK_ROOK) score.and(BLACK_ROOK[(i-2)*(j-2)])
                if (c == ChessType.BLACK_BISHOP) score.and(BLACK_BISHOP[(i-2)*(j-2)])
                if (c == ChessType.BLACK_KNIGHT) score.and(BLACK_KNIGHT[(i-2)*(j-2)])
                if (c == ChessType.BLACK_PAWN) score.and(BLACK_PAWN[(i-2)*(j-2)])

            }
        }
        return (score/100).toDouble()
    }

    private fun isRepeat(text:Node):Boolean {
        var boardHash = ""
        var c = ""

        for (i in 2..9){
            for (j in 2..9){
                c = text.coordinateList[(i*12)+j].type.toString()
                if (c != ChessType.EMPTY.toString()) boardHash.plus(c)
                else boardHash.plus(0)
            }
        }
        if (boardHash.isEmpty() || previousList.last() == boardHash){
            previousList.add(boardHash)
            return false
        }
        return true
    }

    private fun moveQueen(x: Int,y: Int,color: ChessColor){
        moveRook(x,y,color)
        moveBishop(x,y,color)
    }

    private fun moveRook(x: Int, y: Int, color: ChessColor) {

        val source = root.coordinateList[(x*12)+y].type

        for (i in x..9 ){
            val target = root.coordinateList[(i*12)+y].type
            if (target!=source){
                if (target!=ChessType.EMPTY){
                    if (!isSameSide(color,target)) insertTree(x,y,i,y)
                    break
                }
                insertTree(x,y,i,y)
            }
        }

        for (i in x.downTo (2) ){
            val target = root.coordinateList[(i*12)+y].type
            if (target!=source){
                if (target!=ChessType.EMPTY){
                    if (!isSameSide(color,target)) insertTree(x,y,i,y)
                    break
                }
                insertTree(x,y,i,y)
            }
        }

        for (i in y..9 ){
            val target = root.coordinateList[(x*12)+i].type
            if (target!=source){
                if (target!=ChessType.EMPTY){
                    if (!isSameSide(color,target)) insertTree(x,y,x,i)
                    break
                }
                insertTree(x,y,x,i)
            }
        }

        for (i in y.downTo(2) ){
            val target = root.coordinateList[(x*12)+i].type
            if (target!=source){
                if (target!=ChessType.EMPTY){
                    if (!isSameSide(color,target)) insertTree(x,y,x,i)
                    break
                }
                insertTree(x,y,x,i)
            }
        }
    }

    private fun moveBishop(x: Int,y: Int,color: ChessColor){
        var source = root.coordinateList[(x*12)+y].type
        var target = root.coordinateList[(x*12)+y].type

        var s = x
        var t = y
        while(target != ChessType.PLACEHOLDER){
            target = root.coordinateList[(s*12)+t].type
            if(target != ChessType.PLACEHOLDER && target != source){
                if(target != ChessType.EMPTY){
                    if(!isSameSide(color, target))
                        insertTree( x, y, s, t)
                    break
                }
                insertTree( x, y, s, t)
            }
            s++
            t++
        }

        s = x
        t = y
        target = root.coordinateList[(x*12)+y].type
        while(target != ChessType.PLACEHOLDER){
            target = root.coordinateList[(s*12)+t].type
            if(target != ChessType.PLACEHOLDER && target != source){
                if(target != ChessType.EMPTY){
                    if(!isSameSide(color, target))
                        insertTree( x, y, s, t)
                    break
                }
                insertTree( x, y, s, t)
            }
            s--
            t--
        }

        s = x
        t = y
        target = root.coordinateList[(x*12)+y].type
        while(target != ChessType.PLACEHOLDER){
            target = root.coordinateList[(s*12)+t].type
            if(target != ChessType.PLACEHOLDER && target != source){
                if(target != ChessType.EMPTY){
                    if(!isSameSide(color, target))
                        insertTree( x, y, s, t)
                    break
                }
                insertTree( x, y, s, t)
            }
            s-- 
            t++
        }

        s = x
        t = y 
        target = root.coordinateList[(x*12)+y].type
        while(target != ChessType.PLACEHOLDER){
            target = root.coordinateList[(s*12)+t].type
            if(target != ChessType.PLACEHOLDER && target != source){
                if(target != ChessType.EMPTY){
                    if(!isSameSide(color, target))
                        insertTree( x, y, s, t)
                    break;
                }
                insertTree( x, y, s, t)
            }
            s++
            t--
        }
    }

    private fun moveKnight(x: Int,y: Int,color: ChessColor){
        val rowPos = listOf(x-2, x-2, x-1, x-1, x+1, x+1, x+2, x+2)
        val colPos = listOf(y-1, y+1, y-2, y+2, y-2, y+2, y-1, y+1)
        
        for(i in 0..7){
            val r = rowPos[i]
            val c = colPos[i]
            val target = root.coordinateList[(r*12)+c].type
            if(target != ChessType.PLACEHOLDER && !isSameSide(color, target))
                insertTree( x, y, r, c);
        }
    }

    private fun movePawn(x: Int,y: Int,color: ChessColor){
        
        if(color == ChessColor.WHITE){
            var target = root.coordinateList[((x-1)*12)+(y-1)].type
            if(target != ChessType.EMPTY && target != ChessType.PLACEHOLDER && !isSameSide(color, target))
                insertTree( x, y, x-1, y-1)

            target = root.coordinateList[((x-1)*12)+(y+1)].type
            if(target != ChessType.EMPTY && target != ChessType.PLACEHOLDER && !isSameSide(color, target))
                insertTree( x, y, x-1, y+1)

            target = root.coordinateList[((x-1)*12)+(y)].type
            if(target == ChessType.EMPTY)
                insertTree( x, y, x-1, y)

            target = root.coordinateList[((x-2)*12)+(y)].type
            if(x == 8 && target == ChessType.EMPTY && ChessType.EMPTY == root.coordinateList[((x-1)*12)+(y)].type)
            insertTree( x, y, x-2, y);
        }

        else{
            var target = root.coordinateList[((x+1)*12)+(y-1)].type
            if(target != ChessType.EMPTY && target != ChessType.PLACEHOLDER && !isSameSide(color, target))
                insertTree( x, y, x+1, y-1)

            target = root.coordinateList[((x+1)*12)+(y+1)].type
            if(target != ChessType.EMPTY && target != ChessType.PLACEHOLDER && !isSameSide(color, target))
                insertTree( x, y, x+1, y+1)

            target = root.coordinateList[((x+1)*12)+(y)].type
            if(target == ChessType.EMPTY)
                insertTree( x, y, x+1, y);

            target = root.coordinateList[((x+2)*12)+(y)].type
            if(x == 3 && target == ChessType.EMPTY && ChessType.EMPTY == root.coordinateList[((x+1)*12)+(y)].type)
            insertTree( x, y, x+2, y)
        }
    }

    private fun moveKing(x: Int,y: Int,color: ChessColor){
        val r = x-1
        val c = y-1

        for (i in r..(x+1)){
            for (j in c..(y+1)){
                if (r == x && c == y) continue
                val target = root.coordinateList[(r*12)+c].type
                if (target != ChessType.PLACEHOLDER && isSameSide(color,target))
                    insertTree(x,y,r,c)
            }
        }
    }

    private fun minMaxAlphaBeta(depth: Int,state:Boolean,alpha:Double,beta:Double):Double {
        var a = alpha
        var b = beta

        if(depth >= MAX_DEPTH || 0 == root.next.size){
            staticEvals++;
            return evaluate(root)
        }

        // MAXIMIZING
        else if(state){

            for (item in root.next){
                val value = minMaxAlphaBeta(depth+1,false,alpha,beta)
                if (value>a){
                    if (0 == depth) best = item.copy()
                    a = value
                }
                if (a>=b)break
            }
            return a
        }

        // MINIMIZING
        else{

            for (item in root.next){
                val value = minMaxAlphaBeta(depth+1,true,alpha,beta)
                if (value<b){
                    if (0 == depth) best = item.copy()
                    b = value
                }
                if (a>=b)break
            }
            return b
        }
    }

    private fun runAI(){
        nextMove( 0)
        val x = minMaxAlphaBeta(  0, false, LOWEST_SCORE.toDouble(), HIGHEST_SCORE.toDouble())
        changeTurn()
        staticEvals = 0
        Toast.makeText(requireContext(), best.coordinateList.size.toString(), Toast.LENGTH_SHORT).show()
        root = best.copy()

        /*deleteTree(root)*/
        previousList.clear()
        itemChessAdapter.updateList(root.coordinateList)
        locationList.clear()
    }

    private fun kingMoves(x: Int,y: Int,type: ChessType){
        resetBoxColor(locationList)
        locationList.clear()

        if(y > 2 && x > 2 && chessPieceList[((x-1)*12)+(y-1)].type.text.toString() != type.toString()) {
            locationList.add(chessPieceList[((x-1)*12)+(y-1)])
            setBoxColor(locationList.last())
        }
        if(y < 9 && x > 2 && chessPieceList[((x-1)*12)+(y+1)].type.text.toString() != type.toString()) {
            locationList.add(chessPieceList[((x-1)*12)+(y+1)])
            setBoxColor(locationList.last())

        }
        if(x>2 && chessPieceList[((x-1)*12)+(y)].type.text.toString() != type.toString()) {
            locationList.add(chessPieceList[((x-1)*12)+(y)])
            setBoxColor(locationList.last())

        }
        if(x<9 && chessPieceList[((x+1)*12)+(y)].type.text.toString() != type.toString()) {
            locationList.add(chessPieceList[((x+1)*12)+(y)])
            setBoxColor(locationList.last())

        }
        if(y>2 && chessPieceList[((x)*12)+(y-1)].type.text.toString() != type.toString()) {
            locationList.add(chessPieceList[((x)*12)+(y-1)])
            setBoxColor(locationList.last())

        }
        if(y<9 && chessPieceList[((x)*12)+(y+1)].type.text.toString() != type.toString()) {
            locationList.add(chessPieceList[((x)*12)+(y+1)])
            setBoxColor(locationList.last())

        }
        if(y > 2 && x < 9  && chessPieceList[((x+1)*12)+(y-1)].type.text.toString() != type.toString()) {
            locationList.add(chessPieceList[((x+1)*12)+(y-1)])
            setBoxColor(locationList.last())

        }
        if(y < 9 && x < 9 && chessPieceList[((x+1)*12)+(y+1)].type.text.toString() != type.toString()) {
            locationList.add(chessPieceList[((x+1)*12)+(y+1)])
            setBoxColor(locationList.last())

        }
    }

    private fun queenMoves(x: Int,y: Int,type: ChessType){
        resetBoxColor(locationList)
        locationList.clear()

        for (i in (x-1) downTo 2 ){
            if (chessPieceList[(i*12)+y].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(i*12)+y])
                if (setBoxColor(locationList.last()))
                    break
            }
        }

        for (i in (x+1)..9 ){
            if (chessPieceList[(i*12)+y].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(i*12)+y])
                if (setBoxColor(locationList.last()))
                    break
            }
        }

        for (i in (y-1) downTo 2 ){
            if (chessPieceList[(x*12)+i].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(x*12)+i])
                if (setBoxColor(locationList.last()))
                    break
            }
        }

        for (i in (y+1)..9 ){
            if (chessPieceList[(x*12)+i].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(x*12)+i])
                if (setBoxColor(locationList.last()))
                    break
            }
        }
        
        var j = y-1

        for (i in (x-1) downTo 2 ){
            if (j<2)break
            if (chessPieceList[(i*12)+j].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(i*12)+j])
                if (setBoxColor(locationList.last()))
                    break
            }
            j-=1
        }

        j = y+1

        for (i in (x+1)..9 ){
            if (j>9)break
            if (chessPieceList[(i*12)+j].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(i*12)+j])
                if (setBoxColor(locationList.last()))
                    break
            }
            j+=1
        }
        
        j = y+1
        
        for (i in (y-1) downTo 2 ){
            if (j>9)break
            if (chessPieceList[(i*12)+j].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(i*12)+j])
                if (setBoxColor(locationList.last()))
                    break
            }
            j+=1
        }

        j = y-1

        for (i in (y+1)..9 ){
            if (j<2)break
            if (chessPieceList[(i*12)+j].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(i*12)+j])
                if (setBoxColor(locationList.last()))
                    break
            }
            j-=1
        }

    }

    private fun bishopMoves(x: Int,y: Int,type: ChessType){
        resetBoxColor(locationList)
        locationList.clear()

        var j = y-1

        for (i in (x-1) downTo 2 ){
            if (j<2)break
            if (chessPieceList[(i*12)+j].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(i*12)+j])
                if (setBoxColor(locationList.last()))
                    break
            }
            j-=1
        }

        j = y+1

        for (i in (x+1)..9 ){
            if (j>9)break
            if (chessPieceList[(i*12)+j].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(i*12)+j])
                if (setBoxColor(locationList.last()))
                    break
            }
            j+=1
        }

        j = y+1

        for (i in (y-1) downTo 2 ){
            if (j>9)break
            if (chessPieceList[(i*12)+j].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(i*12)+j])
                if (setBoxColor(locationList.last()))
                    break
            }
            j+=1
        }

        j = y-1

        for (i in (y+1)..9 ){
            if (j<2)break
            if (chessPieceList[(i*12)+j].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(i*12)+j])
                if (setBoxColor(locationList.last()))
                    break
            }
            j-=1
        }

    }

    private fun knightMoves(x: Int,y: Int,type: ChessType){
        resetBoxColor(locationList)
        locationList.clear()

        var i = x - 2
        var j = y - 1
        val bool = chessPieceList[(i*12)+j].type.text.toString() != type.toString()
        
        if(i >=2 && j>=2 && bool ) {
            locationList.add(chessPieceList[(i*12)+j])
            setBoxColor(locationList.last())
        }

        i = x - 2
        j = y + 1
        
        if(i >=2 && j<=9 && bool ) {
            locationList.add(chessPieceList[(i*12)+j])
            setBoxColor(locationList.last())
        }

        i = x + 2
        j = y - 1
        if(i <= 9 && j>=2 && bool ) {
            locationList.add(chessPieceList[(i*12)+j])
            setBoxColor(locationList.last())
        }

        i = x + 2
        j = y + 1
        
        if(i <=9 && j<=9 && bool ) {
            locationList.add(chessPieceList[(i*12)+j])
            setBoxColor(locationList.last())
        }

        i = x - 1
        j = y - 2
        
        if(i >=2 && j>=2 && bool ) {
            locationList.add(chessPieceList[(i*12)+j])
            setBoxColor(locationList.last())
        }

        i = x + 1
        j = y - 2
        
        if(i <=9 && j>=2 && bool ) {
            locationList.add(chessPieceList[(i*12)+j])
            setBoxColor(locationList.last())
        }

        i = x - 1
        j = y + 2
        
        if(i >=2 && j<=9 && bool ) {
            locationList.add(chessPieceList[(i*12)+j])
            setBoxColor(locationList.last())
        }

        i = x + 1
        j = y + 2
        if(i <=9 && j<=9 && bool ) {
            locationList.add(chessPieceList[(i*12)+j])
            setBoxColor(locationList.last())
        }
    }

    private fun rookMoves(x: Int,y: Int,type: ChessType){
        resetBoxColor(locationList)
        locationList.clear()

        for (i in (x-1) downTo 2 ){
            if (chessPieceList[(i*12)+y].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(i*12)+y])
                if (setBoxColor(locationList.last()))
                    break
            }
        }

        for (i in (x+1)..9 ){
            if (chessPieceList[(i*12)+y].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(i*12)+y])
                if (setBoxColor(locationList.last()))
                    break
            }
        }

        for (i in (y-1) downTo 2 ){
            if (chessPieceList[(x*12)+i].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(x*12)+i])
                if (setBoxColor(locationList.last()))
                    break
            }
        }

        for (i in (y+1)..9 ){
            if (chessPieceList[(x*12)+i].type.text.toString() == type.toString()) break
            else {
                locationList.add(chessPieceList[(x*12)+i])
                if (setBoxColor(locationList.last()))
                    break
            }
        }

    }

    private fun pawnMoves(x: Int,y: Int,type: ChessType){
        resetBoxColor(locationList)
        locationList.clear()
        if (type.toString().startsWith("WHITE_PAWN")){
            if (x>2 && y>2 && chessPieceList[((x-1)*12)+(y-1)].type.text.toString().startsWith("BLACK") ){
                locationList.add(chessPieceList[((x-1)*12)+(y-1)])
                setBoxColor(locationList.last())
            }
            if (x>2 && y<9 && chessPieceList[((x-1)*12)+(y+1)].type.text.toString().startsWith("BLACK")){
                locationList.add(chessPieceList[((x-1)*12)+(y+1)])
                setBoxColor(locationList.last())
            }
            if (x>2 && (chessPieceList[(x-1)*12+(y)].type.text.toString() == ChessType.EMPTY.toString())){
                locationList.add(chessPieceList[(x-1)*12+(y)])
                setBoxColor(chessPieceList[(x-1)*12+(y)])
                if (type == ChessType.WHITE_PAWN_FIRST && chessPieceList[(x-2)*12+(y)].type.text.toString() == ChessType.EMPTY.toString()){
                    locationList.add(chessPieceList[(x-2)*12+(y)])
                    setBoxColor(locationList.last())
                }
            }
        }
    }

    private fun setBoxColor(itemChessBinding: ItemChessBinding):Boolean{
        itemChessBinding.cardAlpha.toVisible()
        if (itemChessBinding.type.text.toString() != ChessType.EMPTY.toString() && itemChessBinding.type.text.toString() != ChessType.PLACEHOLDER.toString()){
            if (itemChessBinding.type.text.toString().endsWith("KING")) itemChessBinding.cardAlpha.setCardBackgroundColor(requireContext().getColor(R.color.redLight))
            else itemChessBinding.cardAlpha.setCardBackgroundColor(requireContext().getColor(R.color.blueLight))
            return true
        }else{
            itemChessBinding.cardAlpha.setCardBackgroundColor(requireContext().getColor(R.color.greenLight))
        }
        return false
    }

    private fun resetBoxColor(locationList:List<ItemChessBinding>){
        for (item in locationList){
            item.cardAlpha.toGone()
        }
    }

    private fun makeMove(x: Int,y: Int,type: ChessType){
        when(type){
            ChessType.WHITE_KING -> kingMoves(x,y,type)
            ChessType.WHITE_QUEEN -> queenMoves(x,y,type)
            ChessType.WHITE_BISHOP -> bishopMoves(x,y,type)
            ChessType.WHITE_KNIGHT -> knightMoves(x,y,type)
            ChessType.WHITE_ROOK -> rookMoves(x,y,type)
            ChessType.WHITE_PAWN_FIRST,ChessType.WHITE_PAWN -> pawnMoves(x,y,type)
            else -> Toast.makeText(requireContext(), "invalid move", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyLocation(locationList:MutableList<ItemChessBinding>,x:Int,y:Int):Boolean{
        for (item in locationList) if (item.x.text.toString().toInt() == x && item.y.text.toString().toInt() == y) return true
        return false
    }

    private fun movePiece(chessPiece1: ChessPiece,chessPiece2: ChessPiece){
        val x1 = chessPiece1.x
        val y1 = chessPiece1.y

        val x2 = chessPiece2.x
        val y2 = chessPiece2.y

        if (chessPiece1.type == ChessType.WHITE_PAWN_FIRST){
            root.coordinateList[(x1*12)+y1].type = chessPiece2.type
            root.coordinateList[(x2*12)+y2].type = ChessType.WHITE_PAWN
        }else{
            root.coordinateList[(x1*12)+y1].type = chessPiece2.type
            root.coordinateList[(x2*12)+y2].type = chessPiece1.type
        }
        itemChessAdapter.updateList(root.coordinateList)

        if (chessPiece2.type != ChessType.EMPTY) root.materials[chessPiece2.type]!!.minus(1)

        resetBoxColor(locationList)
        locationList.clear()
        changeTurn()
        runAI()
    }

    private fun changeTurn(){
        if (root.currentPlayer == ChessColor.WHITE){
            root.currentPlayer = ChessColor.BLACK
        }else{
            root.currentPlayer = ChessColor.WHITE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationList.clear()
        chessPieceList.clear()
        _binding = null
    }

    companion object{

        const val MAX_DEPTH = 3
        const val LOWEST_SCORE = -10000
        const val HIGHEST_SCORE = 10000
        const val WEIGHT_KING = 1300
        const val WEIGHT_QUEEN = 900
        const val WEIGHT_ROOK = 500
        const val WEIGHT_BISHOP = 330
        const val WEIGHT_KNIGHT = 320
        const val WEIGHT_PAWN = 100

        val WHITE_KING = listOf(
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            20, 20,  0,  0,  0,  0, 20, 20,
            20, 30, 10,  0,  0, 10, 30, 20
        )

        val WHITE_QUEEN = listOf(
            -20,-10,-10, -5, -5,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5,  5,  5,  5,  0,-10,
            -5,  0,  5,  5,  5,  5,  0, -5,
            0,  0,  5,  5,  5,  5,  0, -5,
            -10,  5,  5,  5,  5,  5,  0,-10,
            -10,  0,  5,  0,  0,  0,  0,-10,
            -20,-10,-10, -5, -5,-10,-10,-20
        )

        val WHITE_BISHOP = listOf(
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -20,-10,-10,-10,-10,-10,-10,-20
        )

        val WHITE_KNIGHT = listOf(
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,  0,  0,  0,  0,-20,-40,
            -30,  0, 10, 15, 15, 10,  0,-30,
            -30,  5, 15, 20, 20, 15,  5,-30,
            -30,  0, 15, 20, 20, 15,  0,-30,
            -30,  5, 10, 15, 15, 10,  5,-30,
            -40,-20,  0,  5,  5,  0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50
        )

        val WHITE_ROOK = listOf(
            0,  0,  0,  0,  0,  0,  0,  0,
            5, 10, 10, 10, 10, 10, 10,  5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            0,  0,  0,  5,  5,  0,  0,  0
        )

        val WHITE_PAWN = listOf(
            0,  0,  0,  0,  0,  0,  0,  0,
            50, 50, 50, 50, 50, 50, 50, 50,
            10, 10, 20, 30, 30, 20, 10, 10,
            5,  5, 10, 25, 25, 10,  5,  5,
            0,  0,  0, 20, 20,  0,  0,  0,
            5, -5,-10,  0,  0,-10, -5,  5,
            5, 10, 10,-20,-20, 10, 10,  5,
            0,  0,  0,  0,  0,  0,  0,  0
        )

        val BLACK_KING = listOf(
            20, 30, 10,  0,  0, 10, 30, 20,
            20, 20,  0,  0,  0,  0, 20, 20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30
        )

        val BLACK_QUEEN = listOf(
            -20,-10,-10, -5, -5,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5,  5,  5,  5,  0,-10,
            0,  0,  5,  5,  5,  5,  0, -5,
            -5,  0,  5,  5,  5,  5,  0, -5,
            -10,  5,  5,  5,  5,  5,  0,-10,
            -10,  0,  5,  0,  0,  0,  0,-10,
            -20,-10,-10, -5, -5,-10,-10,-20
        )

        val BLACK_BISHOP = listOf(
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -20,-10,-10,-10,-10,-10,-10,-20
        )

        val BLACK_KNIGHT = listOf(
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,  0,  5,  5,  0,-20,-40,
            -30,  5, 10, 15, 15, 10,  5,-30,
            -30,  0, 15, 20, 20, 15,  0,-30,
            -30,  5, 15, 20, 20, 15,  5,-30,
            -30,  0, 10, 15, 15, 10,  0,-30,
            -40,-20,  0,  0,  0,  0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50
        )

        val BLACK_ROOK = listOf(
            0,  0,  0,  5,  5,  0,  0,  0,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            5, 10, 10, 10, 10, 10, 10,  5,
            0,  0,  0,  0,  0,  0,  0,  0
        )

        val BLACK_PAWN = listOf(
            0,  0,  0,  0,  0,  0,  0,  0,
            5, 10, 10,-20,-20, 10, 10,  5,
            5, -5,-10,  0,  0,-10, -5,  5,
            0,  0,  0, 20, 20,  0,  0,  0,
            5,  5, 10, 25, 25, 10,  5,  5,
            10, 10, 20, 30, 30, 20, 10, 10,
            50, 50, 50, 50, 50, 50, 50, 50,
            0,  0,  0,  0,  0,  0,  0,  0
        )

        private val initialCoordinate = mutableListOf(
            //0
            ChessPiece(0,0,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(0,1,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(0,2,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(0,3,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(0,4,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(0,5,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(0,6,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(0,7,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(0,8,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(0,9,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(0,10,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(0,11,ChessType.PLACEHOLDER,ChessColor.NULL),

            //1
            ChessPiece(1,0,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(1,1,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(1,2,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(1,3,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(1,4,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(1,5,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(1,6,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(1,7,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(1,8,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(1,9,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(1,10,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(1,11,ChessType.PLACEHOLDER,ChessColor.NULL),

            //2
            ChessPiece(2,0,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(2,1,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(2,2,ChessType.BLACK_ROOK,ChessColor.WHITE),
            ChessPiece(2,3,ChessType.BLACK_KNIGHT,ChessColor.BLACK),
            ChessPiece(2,4,ChessType.BLACK_BISHOP,ChessColor.WHITE),
            ChessPiece(2,5,ChessType.BLACK_QUEEN,ChessColor.BLACK),
            ChessPiece(2,6,ChessType.BLACK_KING,ChessColor.WHITE),
            ChessPiece(2,7,ChessType.BLACK_BISHOP,ChessColor.BLACK),
            ChessPiece(2,8,ChessType.BLACK_KNIGHT,ChessColor.WHITE),
            ChessPiece(2,9,ChessType.BLACK_ROOK,ChessColor.BLACK),
            ChessPiece(2,10,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(2,11,ChessType.PLACEHOLDER,ChessColor.NULL),

            //3
            ChessPiece(3,0,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(3,1,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(3,2,ChessType.BLACK_PAWN_FIRST,ChessColor.BLACK),
            ChessPiece(3,3,ChessType.BLACK_PAWN_FIRST,ChessColor.WHITE),
            ChessPiece(3,4,ChessType.BLACK_PAWN_FIRST,ChessColor.BLACK),
            ChessPiece(3,5,ChessType.BLACK_PAWN_FIRST,ChessColor.WHITE),
            ChessPiece(3,6,ChessType.BLACK_PAWN_FIRST,ChessColor.BLACK),
            ChessPiece(3,7,ChessType.BLACK_PAWN_FIRST,ChessColor.WHITE),
            ChessPiece(3,8,ChessType.BLACK_PAWN_FIRST,ChessColor.BLACK),
            ChessPiece(3,9,ChessType.BLACK_PAWN_FIRST,ChessColor.WHITE),
            ChessPiece(3,10,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(3,11,ChessType.PLACEHOLDER,ChessColor.NULL),

            //4
            ChessPiece(4,0,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(4,1,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(4,2,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(4,3,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(4,4,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(4,5,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(4,6,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(4,7,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(4,8,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(4,9,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(4,10,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(4,11,ChessType.PLACEHOLDER,ChessColor.NULL),

            //5
            ChessPiece(5,0,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(5,1,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(5,2,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(5,3,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(5,4,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(5,5,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(5,6,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(5,7,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(5,8,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(5,9,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(5,10,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(5,11,ChessType.PLACEHOLDER,ChessColor.NULL),

            //6
            ChessPiece(6,0,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(6,1,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(6,2,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(6,3,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(6,4,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(6,5,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(6,6,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(6,7,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(6,8,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(6,9,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(6,10,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(6,11,ChessType.PLACEHOLDER,ChessColor.NULL),

            //7
            ChessPiece(7,0,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(7,1,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(7,2,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(7,3,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(7,4,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(7,5,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(7,6,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(7,7,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(7,8,ChessType.EMPTY,ChessColor.BLACK),
            ChessPiece(7,9,ChessType.EMPTY,ChessColor.WHITE),
            ChessPiece(7,10,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(7,11,ChessType.PLACEHOLDER,ChessColor.NULL),

            //8
            ChessPiece(8,0,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(8,1,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(8,2,ChessType.WHITE_PAWN_FIRST,ChessColor.WHITE),
            ChessPiece(8,3,ChessType.WHITE_PAWN_FIRST,ChessColor.BLACK),
            ChessPiece(8,4,ChessType.WHITE_PAWN_FIRST,ChessColor.WHITE),
            ChessPiece(8,5,ChessType.WHITE_PAWN_FIRST,ChessColor.BLACK),
            ChessPiece(8,6,ChessType.WHITE_PAWN_FIRST,ChessColor.WHITE),
            ChessPiece(8,7,ChessType.WHITE_PAWN_FIRST,ChessColor.BLACK),
            ChessPiece(8,8,ChessType.WHITE_PAWN_FIRST,ChessColor.WHITE),
            ChessPiece(8,9,ChessType.WHITE_PAWN_FIRST,ChessColor.BLACK),
            ChessPiece(8,10,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(8,11,ChessType.PLACEHOLDER,ChessColor.NULL),

            //9
            ChessPiece(9,0,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(9,1,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(9,2,ChessType.WHITE_ROOK,ChessColor.BLACK),
            ChessPiece(9,3,ChessType.WHITE_KNIGHT,ChessColor.WHITE),
            ChessPiece(9,4,ChessType.WHITE_BISHOP,ChessColor.BLACK),
            ChessPiece(9,5,ChessType.WHITE_QUEEN,ChessColor.WHITE),
            ChessPiece(9,6,ChessType.WHITE_KING,ChessColor.BLACK),
            ChessPiece(9,7,ChessType.WHITE_BISHOP,ChessColor.WHITE),
            ChessPiece(9,8,ChessType.WHITE_KNIGHT,ChessColor.BLACK),
            ChessPiece(9,9,ChessType.WHITE_ROOK,ChessColor.WHITE),
            ChessPiece(9,10,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(9,11,ChessType.PLACEHOLDER,ChessColor.NULL),

            //10
            ChessPiece(10,0,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(10,1,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(10,2,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(10,3,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(10,4,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(10,5,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(10,6,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(10,7,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(10,8,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(10,9,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(10,10,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(10,11,ChessType.PLACEHOLDER,ChessColor.NULL),

            //11
            ChessPiece(11,0,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(11,1,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(11,2,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(11,3,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(11,4,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(11,5,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(11,6,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(11,7,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(11,8,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(11,9,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(11,10,ChessType.PLACEHOLDER,ChessColor.NULL),
            ChessPiece(11,11,ChessType.PLACEHOLDER,ChessColor.NULL),
        )


        val chessPieceList = mutableListOf<ItemChessBinding>()
        val locationList = mutableListOf<ItemChessBinding>()
        lateinit var currentPiece: ChessPiece
        val initialMaterial = mutableMapOf(
            ChessType.BLACK_PAWN to 8,
            ChessType.BLACK_ROOK to 2,
            ChessType.BLACK_KNIGHT to 2,
            ChessType.BLACK_BISHOP to 2,
            ChessType.BLACK_QUEEN to 1,
            ChessType.BLACK_KING to 1,
            ChessType.WHITE_PAWN to 8,
            ChessType.WHITE_ROOK to 2,
            ChessType.WHITE_KNIGHT to 2,
            ChessType.WHITE_BISHOP to 2,
            ChessType.WHITE_QUEEN to 1,
            ChessType.WHITE_KING to 1,
        )
        private val previousList = mutableListOf<String>()
        private var staticEvals = 0
        private var root = Node(
            initialCoordinate,
            initialMaterial,
            ChessColor.WHITE,
            Pair(2,6),
            Pair(9,6),
            "",
            mutableListOf<Node>(),
        )
        private var best = root.copy()
    }
}

enum class ChessColor(val type:String) {
    BLACK("BLACK"),
    WHITE("WHITE"),
    NULL("NULL");

    override fun toString(): String = type
}

enum class ChessType(val type: String){
    BLACK_KING("BLACK_KING"),
    BLACK_QUEEN("BLACK_QUEEN"),
    BLACK_BISHOP("BLACK_BISHOP"),
    BLACK_KNIGHT("BLACK_KNIGHT"),
    BLACK_ROOK("BLACK_ROOK"),
    BLACK_PAWN("BLACK_PAWN"),
    BLACK_PAWN_FIRST("BLACK_PAWN_FIRST"),
    WHITE_KING("WHITE_KING"),
    WHITE_QUEEN("WHITE_QUEEN"),
    WHITE_BISHOP("WHITE_BISHOP"),
    WHITE_KNIGHT("WHITE_KNIGHT"),
    WHITE_ROOK("WHITE_ROOK"),
    WHITE_PAWN("WHITE_PAWN"),
    WHITE_PAWN_FIRST("WHITE_PAWN_FIRST"),
    EMPTY("EMPTY"),
    PLACEHOLDER("PLACEHOLDER");

    override fun toString(): String = type

}

data class ChessPiece(val x:Int,val y:Int,var type:ChessType,val box:ChessColor)
data class Node(
    var coordinateList:MutableList<ChessPiece>,
    var materials:MutableMap<ChessType,Int>,
    var currentPlayer:ChessColor,
    var blackKingPos:Pair<Int,Int>,
    var whiteKingPos:Pair<Int,Int>,
    var mv:String,
    var next:MutableList<Node>
)
