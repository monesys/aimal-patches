package app.aimal.extension.streaming;

import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Locale;

/** Finds the player's visible intro-skip action by its user-facing label. */
final class IntroSkip {
    private static WeakReference<View> lastClicked = new WeakReference<>(null);
    private static long lastClickTime;

    private IntroSkip() {
    }

    static void apply(View root) {
        if (root == null) return;
        ArrayDeque<View> pending = new ArrayDeque<>();
        pending.add(root);
        while (!pending.isEmpty()) {
            View view = pending.removeFirst();
            if (view.getVisibility() != View.VISIBLE) continue;

            CharSequence label = view.getContentDescription();
            if (view instanceof TextView && (label == null || label.length() == 0)) {
                label = ((TextView) view).getText();
            }
            if (isIntroResource(view) || isSkipIntro(label)) {
                clickOnce(clickableAncestor(view));
                return;
            }

            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;
                for (int i = 0; i < group.getChildCount(); i++) pending.add(group.getChildAt(i));
            }
        }
    }

    /** Handles Disney+'s native skip-button-ready event without polling. */
    static void onButtonReady(final View button) {
        if (!Prefs.introSkip() || button == null || !isIntroResource(button)) return;
        button.post(new Runnable() {
            @Override
            public void run() {
                if (Prefs.introSkip()) clickOnce(button);
            }
        });
    }

    private static boolean isIntroResource(View view) {
        try {
            return "skipIntro".equals(view.getResources().getResourceEntryName(view.getId()));
        } catch (Throwable ignored) {
            return isSkipIntro(view.getContentDescription());
        }
    }

    private static void clickOnce(View target) {
        long now = SystemClock.uptimeMillis();
        if (target == lastClicked.get() && now - lastClickTime <= 1500) return;
        lastClicked = new WeakReference<>(target);
        lastClickTime = now;
        target.performClick();
        Logger.d("Native intro skip clicked");
    }

    private static View clickableAncestor(View view) {
        View current = view;
        while (!current.isClickable() && current.getParent() instanceof View) {
            current = (View) current.getParent();
        }
        return current;
    }

    private static boolean isSkipIntro(CharSequence value) {
        if (value == null) return false;
        String text = value.toString().trim().toLowerCase(Locale.ROOT);
        return (text.contains("skip") && (text.contains("intro") || text.contains("opening")))
                || text.contains("イントロをスキップ")
                || text.contains("イントロスキップ")
                || text.contains("オープニングをスキップ");
    }
}
