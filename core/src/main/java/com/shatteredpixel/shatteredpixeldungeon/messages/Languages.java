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

package com.shatteredpixel.shatteredpixeldungeon.messages;

import java.util.Locale;

public enum Languages {
	ENGLISH("english",      "en", Status.O_COMPLETE, null, null),
	CHI_SMPL("简体中文",     "zh", Status.__UNREVIEW, new String[]{"Chronie_Lynn_Iwa", "Jinkeloid(zdx00793)", "endlesssolitude"}, new String[]{"931451545", "Budding", "Fatir", "Fishbone", "Hcat", "HoofBumpBlurryface", "Horr_lski", "Lery", "Lyn_0401", "Lyx0527", "Ooooscar", "RainSlide", "ShatteredFlameBlast", "SpaceAnchor", "SunsetGlowTheDOGE", "Teller", "hmdzl001", "leo", "tempest102", "户方狸奴"}),
	KOREAN("한국어",         "ko", Status.__UNREVIEW, new String[]{"Cocoa", "Flameblast12", "GameConqueror", "Korean2017"}, new String[]{"AFS", "N8fall", "WondarRabb1t", "benepaper", "chlrhwnstkd", "ddojin0115", "eeeei", "enjuxx", "hancyel", "linterpreteur", "lemonam", "lsiebnie", "sora0430"}),
	JAPANESE("日本語",       "ja", Status.O_COMPLETE, new String[]{"daingewuvzeevisiddfddd", "oz51199"}, new String[]{"Gosamaru", "NickZhrbin", "Otogiri", "Siraore_Rou", "amama", "grassedge", "kiyofumimanabe", "librada", "mocklike", "tomofumikitano"}),
	;


	public enum Status{
		//below 80% translated languages are not added or removed
		X_UNFINISH, //unfinished, ~80-99% translated
		__UNREVIEW, //unreviewed, but 100% translated
		O_COMPLETE, //complete, 100% reviewed
	}

	private String name;
	private String code;
	private Status status;
	private String[] reviewers;
	private String[] translators;

	Languages(String name, String code, Status status, String[] reviewers, String[] translators){
		this.name = name;
		this.code = code;
		this.status = status;
		this.reviewers = reviewers;
		this.translators = translators;
	}

	public String nativeName(){
		return name;
	}

	public String code(){
		return code;
	}

	public Status status(){
		return status;
	}

	public String[] reviewers() {
		if (reviewers == null) return new String[]{};
		else return reviewers.clone();
	}

	public String[] translators() {
		if (translators == null) return new String[]{};
		else return translators.clone();
	}

	public static Languages matchLocale(Locale locale){
		//special case for chinese traditional, which matches more specifically than other languages
		if (locale.getLanguage().equals("zh") && locale.toString().contains("Hant")){
			return Languages.CHI_TRAD;
		}
		return matchCode(locale.getLanguage());
	}

	public static Languages matchCode(String code){
		for (Languages lang : Languages.values()){
			if (lang.code().equals(code))
				return lang;
		}
		return ENGLISH;
	}

}
