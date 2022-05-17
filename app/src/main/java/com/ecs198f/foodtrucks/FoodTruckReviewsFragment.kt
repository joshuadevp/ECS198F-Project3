package com.ecs198f.foodtrucks

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecs198f.foodtrucks.databinding.FragmentFoodTruckReviewsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FoodTruckReviewsFragment(private val foodTruck: FoodTruck) : Fragment() {
    private lateinit var binding: FragmentFoodTruckReviewsBinding
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodTruckReviewsBinding.inflate(inflater, container, false)
        val recyclerViewAdapter = FoodTruckReviewListRecyclerViewAdapter(listOf())

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("699423245763-insnbbp034ep600msiqfan5g0b2pau67.apps.googleusercontent.com")
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);



        binding.apply {
            foodTruckReviewsRecyclerView.apply {
                adapter = recyclerViewAdapter
                layoutManager = LinearLayoutManager(context)
            }

            foodTruckReviewsSignInButton.setOnClickListener {
                val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
                startActivityForResult(signInIntent, 1000)
            }

            foodTruckReviewsPostButton.setOnClickListener {
                val text = foodTruckReviewsInput.getText().toString()
                (requireActivity() as MainActivity).apply {
                    foodTruckService.postFoodTruckReview(foodTruck.id, "Bearer " + token, PostReview(text))
                        .enqueue(object : Callback<Unit> {
                            override fun onResponse(
                                call: Call<Unit>,
                                response: Response<Unit>
                            ) {
                                Log.d("RESPONSE", response.code().toString())
                            }


                            override fun onFailure(call: Call<Unit>, t: Throwable) {
                                //throw t
                            }
                        })
                }
            }


            (requireActivity() as MainActivity).apply {
                title = foodTruck.name

                foodTruckService.listFoodTruckReviews(foodTruck.id)
                    .enqueue(object : Callback<List<FoodTruckReview>> {
                        override fun onResponse(
                            call: Call<List<FoodTruckReview>>,
                            response: Response<List<FoodTruckReview>>
                        ) {
                            recyclerViewAdapter.updateItems(response.body()!!)
                        }

                        override fun onFailure(call: Call<List<FoodTruckReview>>, t: Throwable) {
                            //throw t
                        }
                    })
            }

            return binding.root
        }
    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(requireActivity())
        updateUI(account)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1000) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }


    private fun updateUI(account: GoogleSignInAccount?){
        if(account != null) {
            token = account.idToken!!
            Log.d("TOKEN",token)
            binding.apply {
                foodTruckReviewsSignInButton.setVisibility(View.GONE)
                foodTruckReviewsPostButton.setVisibility(View.VISIBLE)
                foodTruckReviewsInput.setVisibility(View.VISIBLE)
            }
        } else {
            binding.apply{
                foodTruckReviewsSignInButton.setVisibility(View.VISIBLE)
                foodTruckReviewsPostButton.setVisibility(View.GONE)
                foodTruckReviewsInput.setVisibility(View.GONE)
            }
        }
    }
}