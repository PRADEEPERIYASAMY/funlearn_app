package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.FragmentGameThreeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameThreeFragment : Fragment() {

    private var _binding: FragmentGameThreeBinding? = null
    private val binding
        get() = _binding!!

    private var boardCells = ArrayList<ArrayList<AppCompatImageView>>()
    private var playBoard = Board()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBoard()
        playBoard = Board()
        mapBoardToUi()
        initOnClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initOnClick() {
        binding.buttonStart.setOnClickListener {
            playBoard = Board()
            mapBoardToUi()
        }
    }

    private fun mapBoardToUi() {
        for (i in playBoard.board.indices) {
            for (j in playBoard.board.indices) {
                when (playBoard.board[i][j]) {
                    Board.PLAYER -> {
                        boardCells[i][j].setImageResource(R.drawable.o)
                        boardCells[i][j].isEnabled = false
                    }
                    Board.COMPUTER -> {
                        boardCells[i][j].setImageResource(R.drawable.x)
                        boardCells[i][j].isEnabled = false
                    }
                    else -> {
                        boardCells[i][j].setImageResource(0)
                        boardCells[i][j].isEnabled = true
                    }
                }
            }
        }
    }

    private fun loadBoard() {

        boardCells.add(arrayListOf(binding.xoImage1, binding.xoImage2, binding.xoImage3))
        boardCells.add(arrayListOf(binding.xoImage4, binding.xoImage5, binding.xoImage6))
        boardCells.add(arrayListOf(binding.xoImage7, binding.xoImage8, binding.xoImage9))

        for (i in 0..2) {
            for (j in 0..2) {
                boardCells[i][j].setOnClickListener {
                    clickListener(i,j)
                }
            }
        }
    }

    private fun clickListener(i: Int, j: Int) {
        Toast.makeText(requireContext(), "$i $j", Toast.LENGTH_SHORT).show()

        if (!playBoard.isGameOver) {
            val cell = Cell(i, j)
            playBoard.placeMove(cell, Board.PLAYER)
            playBoard.minimax(0, Board.COMPUTER)
            playBoard.computersMove?.let {
                playBoard.placeMove(it, Board.COMPUTER)
            }
            mapBoardToUi()
        }

        when {
            playBoard.hasComputerWon() -> Toast.makeText(
                requireContext(),
                "Computer won",
                Toast.LENGTH_SHORT
            ).show()
            playBoard.hasPlayerWon() -> Toast.makeText(
                requireContext(),
                "Player won",
                Toast.LENGTH_SHORT
            ).show()
            playBoard.isGameOver -> Toast.makeText(
                requireContext(),
                "tie",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

data class Cell(val i: Int, val j: Int)

class Board {

    companion object {
        const val PLAYER = "O"
        const val COMPUTER = "X"
    }

    val board = Array(3) { arrayOfNulls<String>(3) }

    private val availableCells: List<Cell>
        get() {
            val cells = mutableListOf<Cell>()
            for (i in board.indices) {
                for (j in board.indices) {
                    if (board[i][j].isNullOrEmpty()) {
                        cells.add(Cell(i, j))
                    }
                }
            }
            return cells
        }

    val isGameOver: Boolean
        get() = hasComputerWon() || hasPlayerWon() || availableCells.isEmpty()

    fun hasComputerWon(): Boolean {

        if (board[0][0] == board[1][1] &&
            board[0][0] == board[2][2] &&
            board[0][0] == COMPUTER ||
            board[0][2] == board[1][1] &&
            board[0][2] == board[2][0] &&
            board[0][2] == COMPUTER
        ) {
            return true
        }

        for (i in board.indices) {
            if (
                board[i][0] == board[i][1] &&
                board[i][0] == board[i][2] &&
                board[i][0] == COMPUTER ||
                board[0][i] == board[1][i] &&
                board[0][i] == board[2][i] &&
                board[0][i] == COMPUTER
            ) {
                return true
            }
        }

        return false
    }

    fun hasPlayerWon(): Boolean {

        if (board[0][0] == board[1][1] &&
            board[0][0] == board[2][2] &&
            board[0][0] == PLAYER ||
            board[0][2] == board[1][1] &&
            board[0][2] == board[2][0] &&
            board[0][2] == PLAYER
        ) {
            return true
        }

        for (i in board.indices) {
            if (
                board[i][0] == board[i][1] &&
                board[i][0] == board[i][2] &&
                board[i][0] == PLAYER ||
                board[0][i] == board[1][i] &&
                board[0][i] == board[2][i] &&
                board[0][i] == PLAYER
            ) {
                return true
            }
        }

        return false
    }

    var computersMove: Cell? = null
    fun minimax(depth: Int, player: String): Int {
        if (hasComputerWon()) return +1
        if (hasPlayerWon()) return -1

        if (availableCells.isEmpty()) return 0

        var min = Integer.MAX_VALUE
        var max = Integer.MIN_VALUE

        for (i in availableCells.indices) {
            val cell = availableCells[i]
            if (player == COMPUTER) {
                placeMove(cell, COMPUTER)
                val currentScore = minimax(depth + 1, PLAYER)
                max = Math.max(currentScore, max)

                if (currentScore >= 0) {
                    if (depth == 0) computersMove = cell
                }

                if (currentScore == 1) {
                    board[cell.i][cell.j] = ""
                    break
                }

                if (i == availableCells.size - 1 && max < 0) {
                    if (depth == 0) computersMove = cell
                }
            } else if (player == PLAYER) {
                placeMove(cell, PLAYER)
                val currentScore = minimax(depth + 1, COMPUTER)
                min = Math.min(currentScore, min)

                if (min == -1) {
                    board[cell.i][cell.j] = ""
                    break
                }
            }
            board[cell.i][cell.j] = ""
        }

        return if (player == COMPUTER) max else min
    }

    fun placeMove(cell: Cell, player: String) {
        board[cell.i][cell.j] = player
    }
}
