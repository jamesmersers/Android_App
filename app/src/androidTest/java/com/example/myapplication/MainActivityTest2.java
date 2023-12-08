package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest2 {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testFirstItemImageViewInRecyclerView() {
        onView(ViewMatchers.withId(R.id.recycle_view_books))
                .perform(RecyclerViewActions.scrollToPosition(0));

        onView(withId(R.id.recycle_view_books))
                .check(matches(atPosition(0, hasDescendant(withId(R.id.image_view_book_cover)))))
                .check(matches(withDrawableResource(0,R.drawable.book1)));
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            protected boolean matchesSafely(RecyclerView recyclerView) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }
        };
    }

    public static Matcher<View> withDrawableResource(final int position, final int expectedDrawableResId) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                RecyclerView recyclerView = (RecyclerView) view;
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                if (viewHolder != null) {
                    ImageView imageView = viewHolder.itemView.findViewById(R.id.image_view_book_cover);
                    if (imageView != null) {
                        Drawable drawable = imageView.getDrawable();
                        if (drawable != null) {
                            Drawable expectedDrawable = ContextCompat.getDrawable(view.getContext(), expectedDrawableResId);
                            return drawable.getConstantState().equals(expectedDrawable.getConstantState());
                        }
                    }
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable resource at position " + position + ": " + expectedDrawableResId);
            }
        };
    }
}