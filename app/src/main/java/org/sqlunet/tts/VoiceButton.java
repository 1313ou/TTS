package org.sqlunet.tts;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

public class VoiceButton
{
	private static final String TAG = "VoiceButton";

	/**
	 * Collapsed marker
	 */
	static private final char COLLAPSEDCHAR = '@';


	// I M A G E

	/**
	 * Append spans
	 *
	 * @param sb         spannable string builder
	 * @param imageSpan  image span
	 * @param clickSpan  click span
	 * @param extraSpans possible image style span
	 */
	static private void appendImageSpans(@NonNull final SpannableStringBuilder sb, @Nullable final CharSequence caption, @NonNull final Object imageSpan, @NonNull final Object clickSpan, @NonNull final Object... extraSpans)
	{
		final int from = sb.length();
		sb.append(COLLAPSEDCHAR);
		int to = sb.length();
		sb.setSpan(imageSpan, from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		if (caption != null && caption.length() > 0)
		{
			sb.append(' ');
			sb.append(caption);
		}
		to = sb.length();
		sb.setSpan(clickSpan, from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		for (Object span : extraSpans)
		{
			sb.setSpan(span, from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
	}

	// C L I C K A B L E

	/**
	 * Append clickable image
	 *
	 * @param sb          spannable string builder
	 * @param drawableRes drawable res
	 * @param caption     caption
	 * @param listener    click listener
	 * @param context     context
	 */
	static public void appendClickableImageRes(@NonNull final SpannableStringBuilder sb, @DrawableRes int drawableRes, @NonNull final CharSequence caption, @NonNull final Runnable listener, @NonNull final Context context)
	{
		final Drawable drawable = getDrawable(context, drawableRes);
		appendClickableImage(sb, drawable, caption, listener);
	}

	/**
	 * Append clickable image
	 *
	 * @param sb       spannable string builder
	 * @param drawable drawable
	 * @param caption  caption
	 * @param listener click listener
	 */
	static private void appendClickableImage(@NonNull final SpannableStringBuilder sb, @NonNull final Drawable drawable, @NonNull final CharSequence caption, @NonNull final Runnable listener)
	{
		final ImageSpan span = new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_CENTER);
		final ClickableSpan span2 = new ClickableSpan()
		{
			@Override
			synchronized public void onClick(@NonNull final View view)
			{
				// Log.d(TAG, "Click");
				listener.run();
			}
		};
		appendImageSpans(sb, caption, span, span2);
	}

	/**
	 * Get drawable from resource id
	 *
	 * @param context context
	 * @param resId   resource id
	 * @return drawable
	 */
	@NonNull
	static public Drawable getDrawable(@NonNull final Context context, @DrawableRes final int resId)
	{
		final Drawable drawable = AppCompatResources.getDrawable(context, resId);
		assert drawable != null;
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		return drawable;
	}
}
