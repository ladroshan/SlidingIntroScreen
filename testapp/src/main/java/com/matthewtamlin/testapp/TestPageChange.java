/*
 * Copyright 2016 Matthew Tamlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matthewtamlin.testapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.matthewtamlin.android_utilities_library.helpers.BitmapHelper;
import com.matthewtamlin.android_utilities_library.helpers.ScreenSizeHelper;
import com.matthewtamlin.sliding_intro_screen_library.core.LockableViewPager;
import com.matthewtamlin.sliding_intro_screen_library.pages.Page;
import com.matthewtamlin.sliding_intro_screen_library.pages.ParallaxPage;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the ability to programmatically change the page without swiping. Each button should change
 * the page respectively.
 */
public class TestPageChange extends ThreePageTestBase {
	/**
	 * The index of the page to go to.
	 */
	private static final int GO_TO_PAGE_INDEX = 3;

	/**
	 * Used to identify this class during testing.
	 */
	private static final String TAG = "[TestProgrammatical...]";

	private LinearLayout controlButtons;

	private LinearLayout lockButtons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialiseLayouts();
		initialiseControlButtons();
		initialiseLockButtons();
	}

	@Override
	protected Collection<Page> generatePages(Bundle savedInstanceState) {
		ArrayList<Page> pages = new ArrayList<>();

		final int screenWidth = ScreenSizeHelper.getScreenWidth(getWindowManager());
		final int screenHeight = ScreenSizeHelper.getScreenHeight(getWindowManager());

		final Bitmap frontDots = BitmapHelper
				.decodeSampledBitmapFromResource(getResources(), R.raw.front, screenWidth,
						screenHeight);
		final Bitmap backDots = BitmapHelper
				.decodeSampledBitmapFromResource(getResources(), R.raw.back, screenWidth,
						screenHeight);

		for (int i = 0; i < 2; i++) {
			for (int color : getColors()) {
				final ParallaxPage newPage = ParallaxPage.newInstance();
				newPage.setDesiredBackgroundColor(color);
				newPage.setFrontImage(frontDots);
				newPage.setBackImage(backDots);
				pages.add(newPage);
			}
		}

		return pages;
	}

	private void initialiseLayouts() {
		final LinearLayout outerLayout = new LinearLayout(this);
		outerLayout.setOrientation(LinearLayout.HORIZONTAL);
		getRootView().addView(outerLayout);

		controlButtons = new LinearLayout(this);
		controlButtons.setOrientation(LinearLayout.VERTICAL);
		outerLayout.addView(controlButtons);

		lockButtons = new LinearLayout(this);
		lockButtons.setOrientation(LinearLayout.VERTICAL);
		outerLayout.addView(lockButtons);
	}

	private void initialiseControlButtons() {
		final Button goToFirst = new Button(this);
		controlButtons.addView(goToFirst);
		goToFirst.setText("Go to first page");
		goToFirst.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [go to first page]");

				final int startIndex = getIndexOfCurrentPage();
				goToFirstPage();
				validateLockingConditions(startIndex, 0);
			}
		});

		final Button goToLast = new Button(this);
		controlButtons.addView(goToLast);
		goToLast.setText("Go to last page");
		goToLast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [go to last page]");

				final int startIndex = getIndexOfCurrentPage();
				goToLastPage();
				validateLockingConditions(startIndex, numberOfPages() - 1);
			}
		});

		final Button goToPrevious = new Button(this);
		controlButtons.addView(goToPrevious);
		goToPrevious.setText("Go to previous page");
		goToPrevious.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [go to previous page]");

				final int startIndex = getIndexOfCurrentPage();
				goToPreviousPage();
				validateLockingConditions(startIndex, startIndex == 0 ? 0 : startIndex - 1);
			}
		});

		final Button goToNext = new Button(this);
		controlButtons.addView(goToNext);
		goToNext.setText("Go to next page");
		goToNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [go to next page]");

				final int startIndex = getIndexOfCurrentPage();
				goToNextPage();
				final int lastIndex = numberOfPages() - 1;
				validateLockingConditions(startIndex,
						startIndex == lastIndex ? lastIndex : startIndex + 1);
			}
		});

		final Button goToPage = new Button(this);
		controlButtons.addView(goToPage);
		goToPage.setText("Go to page " + (GO_TO_PAGE_INDEX + 1));
		goToPage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [go to page]");

				final int startIndex = getIndexOfCurrentPage();
				goToPage(GO_TO_PAGE_INDEX);
				validateLockingConditions(startIndex, GO_TO_PAGE_INDEX);
			}
		});
	}

	private void initialiseLockButtons() {
		final Button lockRightSwipe = new Button(this);
		lockButtons.addView(lockRightSwipe);
		lockRightSwipe.setText("Lock swipe");
		lockRightSwipe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [lock right (drag)]");
				setPagingLockMode(LockableViewPager.LockMode.TOUCH_LOCKED);
			}
		});

		final Button lockRightCommand = new Button(this);
		lockButtons.addView(lockRightCommand);
		lockRightCommand.setText("Lock commands");
		lockRightCommand.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [lock left (commands)]");
				setPagingLockMode(LockableViewPager.LockMode.COMMAND_LOCKED);
			}
		});

		final Button lockRightAll = new Button(this);
		lockButtons.addView(lockRightAll);
		lockRightAll.setText("Lock all");
		lockRightAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [lock left (all)]");
				setPagingLockMode(LockableViewPager.LockMode.FULLY_LOCKED);
			}
		});

		final Button unlockRight = new Button(this);
		lockButtons.addView(unlockRight);
		unlockRight.setText("Unlock");
		unlockRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [unlock right]");
				setPagingLockMode(LockableViewPager.LockMode.UNLOCKED);
			}
		});
	}


	private void validateLockingConditions(final int originalPage, final int triedChangingTo) {
		final boolean didChange = (originalPage != getIndexOfCurrentPage());
		final boolean shouldHaveChanged;

		if (triedChangingTo != originalPage) {
			shouldHaveChanged = getPagingLockMode().allowsCommands();
		} else {
			shouldHaveChanged = false;
		}

		Log.d("[should have changed]", "" + shouldHaveChanged);
		Log.d("[did change]", "" + didChange);

		assertThat("locking conditions passed", didChange == shouldHaveChanged);
		Log.d(TAG, "[validate locking conditions] [assertions passed]");
	}
}