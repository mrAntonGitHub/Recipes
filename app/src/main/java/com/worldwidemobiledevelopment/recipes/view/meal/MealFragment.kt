package com.worldwidemobiledevelopment.recipes.view.meal


import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso
import com.worldwidemobiledevelopment.recipes.R
import kotlinx.android.synthetic.main.fragment_meal.*
import technolifestyle.com.imageslider.FlipperLayout
import technolifestyle.com.imageslider.FlipperView
import technolifestyle.com.imageslider.pagetransformers.ZoomOutPageTransformer


class MealFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_meal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Set Image into the flipperView using url
//        view.setImageUrl("https://source.unsplash.com/random") { imageView, image ->
//            // Load image (url) into the imageview using any image loading library of your choice
//            // E.g. Picasso.get().load(image as String).into(imageView);
//        }
//
//        //Set Image using Drawable resource
//        view.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.placeholder)) { imageView, image ->
//            imageView.setImageDrawable(image as Drawable)
//        }
//
//        //Set Image using Bitmap image
//        view.setImageBitmap(bitmapImage) { imageView, image ->
//            imageView.setImageBitmap(image as Bitmap)
//        }

        mealName.text = arguments?.get("name").toString()


        val url =
            arrayOf("https://i.ytimg.com/vi/BKhiMN53DZM/maxresdefault.jpg",
                "https://aloha-plus.ru/wp-content/uploads/2018/08/rabstol_net_elephant_07.jpg",
                "https://i.pinimg.com/originals/18/40/72/184072abb72399c23ab635faaa0a94ad.jpg")

        val flipperLayout = requireActivity().findViewById<FlipperLayout>(R.id.flipper_layout)

        val initSize = main_appbar.layoutParams.height
        val flipperViewList: ArrayList<FlipperView> = ArrayList()
        for (i in url.indices) {
            val view = FlipperView(requireContext())
                .setDescriptionBackgroundColor(Color.TRANSPARENT)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setImage(url[i]) { flipperImageView, image ->
                    Picasso.get().load(image as String).into(flipperImageView)
                }
            view.setOnFlipperClickListener(object : FlipperView.OnFlipperClickListener {
                override fun onFlipperClick(flipperView: FlipperView) {

                }
            })
            flipperViewList.add(view)
        }

        flipperLayout.addFlipperViewList(flipperViewList)
//        flipperLayout.addPageTransformer(false, ZoomOutPageTransformer())
        flipperLayout.showInnerPagerIndicator()
        flipperLayout.setCircleIndicatorHeight(20)
        flipperLayout.setIndicatorBackground(android.R.color.transparent)
//        val view = FlipperView(requireContext())
//        view.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//        view.setImage(R.drawable.error) { imageView, image ->
//            imageView.setImageDrawable(image as Drawable)
//        }
//        flipperLayout.addFlipperView(view)







        main_collapsing.setOnClickListener {
            Toast.makeText(requireContext(), "collaps", Toast.LENGTH_SHORT).show()


            flipperLayout.setOnClickListener { Toast.makeText(
                requireContext(),
                "flipper",
                Toast.LENGTH_SHORT
            ).show()  }

            main_appbar.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    "AppBar",
                    Toast.LENGTH_SHORT
                ).show() }

        }


        val w = requireActivity().window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        (requireActivity() as AppCompatActivity).setSupportActionBar(main_toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = null
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        main_appbar.addOnOffsetChangedListener(this)


        val name = arguments?.get("name")
//        nameTest.text = name.toString()
    }

    override fun onStop() {
        super.onStop()
        val w = requireActivity().window
        w.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (verticalOffset == 0) {
            // Fully expanded
//            main_toolbar.navigationIcon?.setColorFilter(ResourcesCompat.getColor(resources, R.color.white, null), PorterDuff.Mode.SRC_ATOP)
        } else {
            // Not fully expanded or collapsed
//            main_toolbar.navigationIcon?.setColorFilter(ResourcesCompat.getColor(resources, R.color.black, null), PorterDuff.Mode.SRC_ATOP)
        }
    }
}