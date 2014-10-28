package dintorf.mmmf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by dintorf on 10/17/14.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = { "Main Stage", "Local Stage", "Food", "Drinks", "Shopping", "Deals" };

    protected Bundle b;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Lineup fragment activity
//                return new Lineup();
            case 1:
                // Lineup fragment activity
//                return new LocalLineupFragment();
            case 2:
                // Food fragment activity
//                return new FoodActivity();
            case 3:
                // Drink fragment activity
//                return new DrinksActivity();
            case 4:
                // Shop fragment activity
//                return new ShopActivity();
            case 5:
                // Deals fragment activity
//                return new DealsActivity();
        }

        return null;
    }

}