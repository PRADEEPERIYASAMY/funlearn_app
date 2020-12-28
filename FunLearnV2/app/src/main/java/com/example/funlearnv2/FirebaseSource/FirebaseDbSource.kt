package com.example.funlearnv2.FirebaseSource

import com.example.funlearnv2.repository.models.FirebaseDbModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class FirebaseDbSource @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
) {
    fun fetchFirebaseDbData(): FirebaseDbModels {
        val done = CountDownLatch(1)
        var firebaseDbModels: FirebaseDbModels? = null
        firebaseDatabase.reference.child("FirebaseDbModels")
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        firebaseDbModels = snapshot.getValue(FirebaseDbModels::class.java)
                        done.countDown()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
            )
        try {
            done.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return firebaseDbModels!!
        /*alphaRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val n = dataSnapshot.child("Names").child((id + 1).toString()).value.toString()
                    val img = dataSnapshot.child("Images").child((id + 1).toString()).value.toString()
                    names = Arrays.asList(*n.split("----------").toTypedArray())
                    images = Arrays.asList(*img.split("----------").toTypedArray())
                    recyclerView = findViewById(R.id.alphabet_word_recycler)
                    recyclerView.setHasFixedSize(true)
                    recyclerView.setLayoutManager(GridLayoutManager(applicationContext, 2))
                    val alphabetWordsAdapter = AlphabetWordsAdapter(applicationContext, id)
                    recyclerView.setAdapter(alphabetWordsAdapter)
                    wordsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val word = dataSnapshot.child("Words").value.toString().split("----------").toTypedArray()[id]
                                for (i in word.split(ListOfAlphabets.alphabets[id]).toTypedArray().indices) {
                                    alphabetWord.add(word.split(ListOfAlphabets.alphabets[id]).toTypedArray()[i])
                                }
                                for (i in alphabetWord.indices) {
                                    val text = "<font color=#F9333333>" + alphabetWord[i] + "</font>"
                                    val text2 = "<font color=#FF0000>" + ListOfAlphabets.alphabets[id] + "</font>"
                                    val text3 = "<font color=#FF0000>" + ListOfAlphabets.alphabetsUpper[id] + "</font>"
                                    if (i == 0 && alphabetWord[0].length == 0) {
                                        alphabetSentence!!.append(Html.fromHtml(text))
                                        alphabetSentence!!.append(Html.fromHtml(text3))
                                    } else if (i < alphabetWord.size - 1) {
                                        alphabetSentence!!.append(Html.fromHtml(text))
                                        alphabetSentence!!.append(Html.fromHtml(text2))
                                    } else {
                                        alphabetSentence!!.append(Html.fromHtml(text))
                                    }
                                }
                                textToSpeech = TextToSpeech(applicationContext) { status ->
                                    if (status != TextToSpeech.ERROR) {
                                        textToSpeech!!.language = Locale.UK
                                    }
                                }
                                val onClickListener = View.OnClickListener { v ->
                                    mediaPlayer!!.start()
                                    Handler().postDelayed({
                                        when (v.id) {
                                            R.id.alphabet_speak_button -> {
                                                c1!!.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.button))
                                                textToSpeech!!.speak(alphabetSmall!!.text.toString(), TextToSpeech.QUEUE_FLUSH, null)
                                            }
                                            R.id.alphabet_sentence_speak_button -> {
                                                c2!!.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.button))
                                                textToSpeech!!.speak(alphabetSentence!!.text.toString(), TextToSpeech.QUEUE_FLUSH, null)
                                            }
                                        }
                                    }, 250)
                                }
                                letter!!.setOnClickListener(onClickListener)
                                sentence!!.setOnClickListener(onClickListener)
                            }
                            progressBar.visibility = View.INVISIBLE
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                }
            }
        })*/
    }
}
