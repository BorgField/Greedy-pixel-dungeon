/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.HashMap;

public class RenderedTextBlock extends Component {

	private int maxWidth = Integer.MAX_VALUE;
	public int nLines;

	private static final RenderedText SPACE = new RenderedText();
	private static final RenderedText NEWLINE = new RenderedText();

	protected String text;
	protected String[] tokens = null;
	protected ArrayList<RenderedText> words = new ArrayList<>();
	protected boolean multiline = false;

	private int size;
	private float zoom;
	private int color = -1;

	private int hightlightColor = Window.TITLE_COLOR;
	private int redColor = Window.R_COLOR;
	private int greenColor = Window.G_COLOR;
	private int blueColor = Window.B_COLOR;
	private int pinkColor = Window.Pink_COLOR;
	private int deepColor = Window.DeepPK_COLOR;
	private int blackColor = Window.CBLACK;
	private int cyanColor = Window.CYAN_COLOR;

	private boolean highlightingEnabled = true;
	private boolean redEnabled = true;
	private boolean greenEnabled = true;
	private boolean blueEnabled = true;
	private boolean pinkEnabled = true;
	private boolean deepEnabled = true;
	private boolean blackEnabled = true;
	private boolean cyanEnabled = true;

	public static final int LEFT_ALIGN = 1;
	public static final int CENTER_ALIGN = 2;
	public static final int RIGHT_ALIGN = 3;
	private int alignment = LEFT_ALIGN;

	public RenderedTextBlock(int size){
		this.size = size;
	}

	public RenderedTextBlock(String text, int size){
		this.size = size;
		text(text);
	}

	public void text(String text){
		this.text = text;

		if (text != null && !text.equals("")) {

			tokens = Game.platform.splitforTextBlock(text, multiline);

			build();
		}
	}

	//for manual text block splitting, a space between each word is assumed
	public void tokens(String... words){
		StringBuilder fullText = new StringBuilder();
		for (String word : words) {
			fullText.append(word);
		}
		text = fullText.toString();

		tokens = words;
		build();
	}

	public void text(String text, int maxWidth){
		this.maxWidth = maxWidth;
		multiline = true;
		text(text);
	}

	public String text(){
		return text;
	}

	public void maxWidth(int maxWidth){
		if (this.maxWidth != maxWidth){
			this.maxWidth = maxWidth;
			multiline = true;
			text(text);
		}
	}

	public int maxWidth(){
		return maxWidth;
	}

	private synchronized void build(){
		if (tokens == null) return;

		clear();
		words = new ArrayList<>();
		boolean highlighting = false;
		boolean redHighlighting = false;
		boolean greenHighlighting = false;
		boolean blueHighlighting = false;
		boolean pinkHighlighting = false;
		boolean deepHighlighting = false;
		boolean blackHighlighting = false;
		boolean cyanHighlighting = false;

		for (String str : tokens){
			// 处理所有颜色标记
			if (str.equals("_") && highlightingEnabled) {
				highlighting = !highlighting;
			} else if (str.equals("{") && redEnabled) {
				redHighlighting = !redHighlighting;
			} else if (str.equals("}") && greenEnabled) {
				greenHighlighting = !greenHighlighting;
			} else if (str.equals("**") && blueEnabled) {
				blueHighlighting = !blueHighlighting;
			} else if (str.equals("[") && pinkEnabled) {
				pinkHighlighting = !pinkHighlighting;
			} else if (str.equals("]") && deepEnabled) {
				deepHighlighting = !deepHighlighting;
			} else if (str.equals("|") && blackEnabled) {
				blackHighlighting = !blackHighlighting;
			} else if (str.equals("*") && cyanEnabled) {
				cyanHighlighting = !cyanHighlighting;
			} else if (str.equals("\n")){
				words.add(NEWLINE);
			} else if (str.equals(" ")){
				words.add(SPACE);
			} else {
				RenderedText word = new RenderedText(str, size);

				/*
			{ = 红色
			} = 绿色
			* = 蓝色
			[ = 粉色
			] = 紫色
			| = 黑色
			_ = 青色 */

				// 应用颜色优先级
				if (highlighting) {
					word.hardlight(hightlightColor);
				} else if (redHighlighting) {
					word.hardlight(redColor);
				} else if (greenHighlighting) {
					word.hardlight(greenColor);
				} else if (blueHighlighting) {
					word.hardlight(blueColor);
				} else if (pinkHighlighting) {
					word.hardlight(pinkColor);
				} else if (deepHighlighting) {
					word.hardlight(deepColor);
				} else if (blackHighlighting) {
					word.hardlight(blackColor);
				} else if (cyanHighlighting) {
					word.hardlight(cyanColor);
				} else if (color != -1) {
					word.hardlight(color);
				}

				word.scale.set(zoom);

				words.add(word);
				add(word);

				if (height < word.height()) height = word.height();
			}
		}
		layout();
	}

	public synchronized void zoom(float zoom){
		this.zoom = zoom;
		for (RenderedText word : words) {
			if (word != null) word.scale.set(zoom);
		}
		layout();
	}

	public synchronized void hardlight(int color){
		this.color = color;
		for (RenderedText word : words) {
			if (word != null) word.hardlight( color );
		}
	}

	public synchronized void resetColor(){
		this.color = -1;
		for (RenderedText word : words) {
			if (word != null) word.resetColor();
		}
	}

	public synchronized void alpha(float value){
		for (RenderedText word : words) {
			if (word != null) word.alpha( value );
		}
	}

	public synchronized void setHightlighting(boolean enabled){
		setHightlighting(enabled, Window.TITLE_COLOR);
	}

	public synchronized void setHightlighting(boolean enabled, int color){
		if (enabled != highlightingEnabled || color != hightlightColor) {
			hightlightColor = color;
			highlightingEnabled = enabled;
			build();
		}
	}

	// 红色高亮设置方法
	public synchronized void setRedHighlighting(boolean enabled) {
		setRedHighlighting(enabled, Window.R_COLOR);
	}

	public synchronized void setRedHighlighting(boolean enabled, int color) {
		if (enabled != redEnabled || color != redColor) {
			redColor = color;
			redEnabled = enabled;
			build();
		}
	}

	// 绿色高亮设置方法
	public synchronized void setGreenHighlighting(boolean enabled) {
		setGreenHighlighting(enabled, Window.G_COLOR);
	}

	public synchronized void setGreenHighlighting(boolean enabled, int color) {
		if (enabled != greenEnabled || color != greenColor) {
			greenColor = color;
			greenEnabled = enabled;
			build();
		}
	}

	// 蓝色高亮设置方法
	public synchronized void setBlueHighlighting(boolean enabled) {
		setBlueHighlighting(enabled, Window.B_COLOR);
	}

	public synchronized void setBlueHighlighting(boolean enabled, int color) {
		if (enabled != blueEnabled || color != blueColor) {
			blueColor = color;
			blueEnabled = enabled;
			build();
		}
	}

	// 粉色高亮设置方法
	public synchronized void setPinkHighlighting(boolean enabled) {
		setPinkHighlighting(enabled, Window.Pink_COLOR);
	}

	public synchronized void setPinkHighlighting(boolean enabled, int color) {
		if (enabled != pinkEnabled || color != pinkColor) {
			pinkColor = color;
			pinkEnabled = enabled;
			build();
		}
	}

	// 紫色高亮设置方法
	public synchronized void setDeepHighlighting(boolean enabled) {
		setDeepHighlighting(enabled, Window.DeepPK_COLOR);
	}

	public synchronized void setDeepHighlighting(boolean enabled, int color) {
		if (enabled != deepEnabled || color != deepColor) {
			deepColor = color;
			deepEnabled = enabled;
			build();
		}
	}

	// 黑色高亮设置方法
	public synchronized void setBlackHighlighting(boolean enabled) {
		setBlackHighlighting(enabled, Window.CBLACK);
	}

	public synchronized void setBlackHighlighting(boolean enabled, int color) {
		if (enabled != blackEnabled || color != blackColor) {
			blackColor = color;
			blackEnabled = enabled;
			build();
		}
	}

	public synchronized void setCyanHighlighting(boolean enabled) {
		setCyanHighlighting(enabled, Window.CYAN_COLOR);
	}

	public synchronized void setCyanHighlighting(boolean enabled, int color) {
		if (enabled != cyanEnabled || color != cyanColor) {
			cyanColor = color;
			cyanEnabled = enabled;
			build();
		}
	}

	public synchronized void invert(){
		if (words != null) {
			for (RenderedText word : words) {
				if (word != null) {
					word.ra = 0.77f;
					word.ga = 0.73f;
					word.ba = 0.62f;
					word.rm = -0.77f;
					word.gm = -0.73f;
					word.bm = -0.62f;
				}
			}
		}
	}

	public synchronized void align(int align){
		alignment = align;
		layout();
	}

	@Override
	protected synchronized void layout() {
		super.layout();
		float x = this.x;
		float y = this.y;
		float height = 0;
		nLines = 1;

		ArrayList<ArrayList<RenderedText>> lines = new ArrayList<>();
		ArrayList<RenderedText> curLine = new ArrayList<>();
		lines.add(curLine);

		width = 0;
		for (int i = 0; i < words.size(); i++){
			RenderedText word = words.get(i);
			if (word == SPACE){
				x += 1.667f;
			} else if (word == NEWLINE) {
				//newline
				y += height+2f;
				x = this.x;
				nLines++;
				curLine = new ArrayList<>();
				lines.add(curLine);
			} else {
				if (word.height() > height) height = word.height();

				float fullWidth = word.width();
				int j = i+1;

				//this is so that words split only by highlighting are still grouped in layout
				//Chinese/Japanese always render every character separately without spaces however
				while (Messages.lang() != Languages.CHI_SMPL && Messages.lang() != Languages.CHI_TRAD
						&& Messages.lang() != Languages.JAPANESE
						&& j < words.size() && words.get(j) != SPACE && words.get(j) != NEWLINE){
					fullWidth += words.get(j).width() - 0.667f;
					j++;
				}

				if ((x - this.x) + fullWidth - 0.001f > maxWidth && !curLine.isEmpty()){
					y += height+2f;
					x = this.x;
					nLines++;
					curLine = new ArrayList<>();
					lines.add(curLine);
				}

				word.x = x;
				word.y = y;
				PixelScene.align(word);
				x += word.width();
				curLine.add(word);

				if ((x - this.x) > width) width = (x - this.x);

				//Note that spacing currently doesn't factor in halfwidth and fullwidth characters
				//(e.g. Ideographic full stop)
				x -= 0.667f;

			}
		}
		this.height = (y - this.y) + height;

		if (alignment != LEFT_ALIGN){
			for (ArrayList<RenderedText> line : lines){
				if (line.size() == 0) continue;
				float lineWidth = line.get(line.size()-1).width() + line.get(line.size()-1).x - this.x;
				if (alignment == CENTER_ALIGN){
					for (RenderedText text : line){
						text.x += (width() - lineWidth)/2f;
						PixelScene.align(text);
					}
				} else if (alignment == RIGHT_ALIGN) {
					for (RenderedText text : line){
						text.x += width() - lineWidth;
						PixelScene.align(text);
					}
				}
			}
		}
	}
}
