////////////////////////////////////////////////////////////////////////////////////////////////////
//
// GUIStyler
//
// * Minimal fancy looks for GUIs
// (C) 2019 Jonathan Reus
//
// Copyright (C) <2016>
// by Darien Brito
// http://www.darienbrito.com
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <http://www.gnu.org/licenses/>.

GUIStyler {
	var <master, <skin, skinObj;
	var <alpha, <>backgroundColor, <>stringColor;
	var <buttonSize, <textSize, <sliderSize;
	var <>gadgetWidth, <>gadgetHeight, <>font;
	var window, <marginW, <marginH, <decorator;
	var <stdFactor; //standard factor for spacing
	var <highLightColor, <>knobSize, <thumbSize;
	var <backgroundSl, <onColor, <flowLayout;
	var <buttonWidth, <ezSizeVert, <ezSizeHorz;
	var <ezSizeW, <ezSizeH, fontType;

	*new {|master, skin = \black|
		^super.newCopyArgs(master, skin).init;
	}

	init {
		skinObj = GUISkins(skin);
		alpha = skinObj.alpha;
		backgroundColor = skinObj.backgroundColor;
		stringColor = skinObj.stringColor;
		fontType = skinObj.fontType;
		font = skinObj.font;
		highLightColor = skinObj.highLightColor;
		backgroundSl = skinObj.backgroundSl;
		onColor = skinObj.onColor;
		this.makeDefaultSizes();
	}

	makeDefaultSizes {
		gadgetWidth = 50;
		gadgetHeight = 20;
		buttonWidth = gadgetWidth * 2;
		buttonSize = buttonWidth@gadgetHeight;
		textSize = gadgetWidth@gadgetHeight;
		ezSizeW = 280;
		ezSizeH = 220;
		sliderSize = gadgetWidth@180;
		ezSizeVert = gadgetWidth@ezSizeH;
		ezSizeHorz = ezSizeW@gadgetHeight;
		marginW = 2;
		marginH = 2;
		stdFactor = 32;
		knobSize = 0;
		thumbSize = 10;
	}


	getView {|name, bounds, scroll=false, margin, gap, parent|
		var view;
		if(parent.isNil) { parent = master };
		if(bounds.isKindOf(Point)) { bounds = Rect(0,0,bounds.x,bounds.y) };
		if(margin.isNil) { margin = 0@0 };
		if(gap.isNil) { margin = 0@0 };

		if(scroll) {
			view = ScrollView(parent, bounds);
		} {
			view = View(parent, bounds);
		};

		view.decorator = FlowLayout(bounds, margin, gap);
		view.alpha_(alpha).alwaysOnTop_(true).background_(backgroundColor);
		^view;
	}


	getButton {|parent, labelOn, labelOff = nil|
		var button = Button(parent, buttonSize)
		.font_(font);
		if(labelOff.isNil){
			^button.states_([ [labelOn, stringColor, backgroundColor] ]);
		}{
			^button.states_([ [labelOn, stringColor, backgroundColor], [labelOff,stringColor, onColor] ]);
		}
		^button;
	}

	getSizableButton { |parent, labelOn, labelOff = nil, size|
		var button = Button(parent, size)
		.font_(font);
		if(labelOff.isNil){
			^button.states_([ [labelOn, stringColor, backgroundColor] ]);
		}{
			^button.states_([ [labelOn, stringColor, backgroundColor], [labelOff,stringColor, onColor] ]);
		}
		^button;
	}

	getSubtitleText { |parent, text, decorator, fontSize = 10, bold = true,align=\center,width = nil|
		if(width == nil){width = decorator.indentedRemaining.width};
		^StaticText(parent, (width)@gadgetHeight )
		.align_(align)
		.font_(Font(fontType, fontSize, bold))
		.string_(text)
		.stringColor_(stringColor)
		.background_(backgroundColor);
	}

	getText { |parent, text|
		^StaticText(parent, textSize)
		.align_(\center)
		.font_(font)
		.string_( text )
		.stringColor_(stringColor)
		.background_(backgroundColor);
	}

	getMultiLineText {|parent, bounds, fontSize = 10|
		^TextView(parent, bounds)
		.font_(Font(fontType, fontSize))
		.stringColor_(stringColor)
		.background_(backgroundColor)
		.hasVerticalScroller_(true)
		.editable_(false);
	}

  getTextField {|parent, width, fontSize=10|
    ^TextField(parent, Rect(0,0,width,gadgetHeight))
    .font_(Font("Courier", fontSize))
    .background_(Color.black)
    .stringColor_(Color.white);
  }

	getTextEdit {|parent, bounds, fontSize = 10|
		var v;
		QPalette.dark.baseText = Color.white;
		QPalette.dark.highlightText = Color.white;
		QPalette.dark.highlightText = Color.white;
		QPalette.dark.window = Color.black;

		^TextView(parent, bounds)
		.font_(Font("Courier", fontSize))
		.stringColor_(Color.white)
		.background_(Color.black)
		.hasVerticalScroller_(true)
		.hasHorizontalScroller_(true)
		.enterInterpretsSelection_(true)
		.autohidesScrollers_(false)
		.editable_(true).palette_(QPalette.dark)
		.keyDownAction_({|view, char, modifiers, unicode, keycode, key|
			if(modifiers == 131072 && unicode == 13) {
				var pos, evalme = view.selectedString;
				// SHIFT+ENTER Evaluate Line/Selection.
				pos = view.selectionStart;
				"EVALUATE % at %: %".format(evalme.size, pos, "[[[[[["++evalme++"]]]]]").postln;
				//view.setString(evalme, pos);
				if(evalme.split($\n).size > 1) {
					evalme.interpret;
				};
			};
		});

	}
	//e = "Hello"
	//e[..2] ++ e[3..]

	getSizableText { |parent, text, width, align = \center, fontSize = 10, bold = false, bgcolor|
		var c, height = gadgetHeight;
		if(bgcolor == nil) { c = backgroundColor; } { c = bgcolor; };
    if(height < fontSize) { height = fontSize };

		^StaticText(parent, width@height)
		.align_(align)
		.font_(Font(fontType, fontSize, bold))
		.string_( text )
		.stringColor_(stringColor)
		.background_(c);
	}

	getSlider { |parent, orientation = \v|
		^SmoothSlider( parent, sliderSize)
		.orientation_(orientation)
		.knobSize_(knobSize).thumbSize_(thumbSize)
		.hilightColor_(highLightColor)
		.value_(0.5)
		.background_(backgroundSl)
		.align_(\left)
		.font_(font);
	}

	getSizableSlider { |parent, bounds, orientation = \v|
		^SmoothSlider( parent, bounds)
		.orientation_(orientation)
		.knobSize_(knobSize).thumbSize_(thumbSize)
		.hilightColor_(highLightColor)
		.value_(0.5)
		.background_(backgroundSl)
		.align_(\left)
		.font_(font);
	}

	getNumberBox { |parent, step|
		^NumberBox(parent, buttonWidth@gadgetHeight)
		.background_(backgroundSl)
		.align_(\left)
		.step_(step)
		.font_(font);
	}

	getSizableNumberBox { |parent, size, step|
		^NumberBox(parent, size)
		.background_(backgroundSl)
		.align_(\left)
		.step_(step)
		.font_(font);
	}

	getRangeSlider { |parent|
		^SmoothRangeSlider(parent, sliderSize)
		.knobSize_(knobSize).thumbSize_(thumbSize)
		.hilightColor_(highLightColor)
		.value_(0.5)
		.background_(backgroundSl)
		.align_(\left)
		.value_([0, 0]);
	}

	getEZSlider { |parent, label, spec, orientation = \vert|
		var ezSmooth;
		if(orientation == \vert) {
			ezSmooth = EZSmoothSliderAntialias(parent, ezSizeVert, label, spec, layout: orientation)
		} {
			ezSmooth = EZSmoothSliderAntialias(parent, ezSizeHorz, label, spec, layout: orientation)
		};
		ezSmooth.sliderView
		.knobSize_(knobSize).thumbSize_(thumbSize)
		.hilightColor_(highLightColor)
		.value_(0.5)
		.background_(backgroundSl)
		.align_(\center)
		.value_([0, 0]);
		ezSmooth.numberView
		.font_(font)
		.stringColor_(stringColor)
		.normalColor_(stringColor)
		.align_(\center)
		.background_(backgroundColor);
		ezSmooth.labelView
		.align_(\center)
		.font_(font)
		.stringColor_(stringColor)
		.background_(backgroundColor);
		^ezSmooth;
	}

	getSizableEZSlider { |parent, label, bounds, spec, orientation = \vert|
		var ezSmooth;
		if(orientation == \vert) {
			ezSmooth = EZSmoothSliderAntialias(parent, bounds, label, spec, layout: orientation)
		} {
			ezSmooth = EZSmoothSliderAntialias(parent, bounds, label, spec, layout: orientation)
		};
		ezSmooth.sliderView
		.knobSize_(knobSize).thumbSize_(thumbSize)
		.hilightColor_(highLightColor)
		.value_(0.5)
		.background_(backgroundSl)
		.align_(\center)
		.value_([0, 0]);
		ezSmooth.numberView
		.font_(font)
		.stringColor_(stringColor)
		.normalColor_(stringColor)
		.align_(\center)
		.background_(backgroundColor);
		ezSmooth.labelView
		.align_(\center)
		.font_(font)
		.stringColor_(stringColor)
		.background_(backgroundColor)
		^ezSmooth;
	}

	getEZRanger { |parent, label, spec, orientation = \vert|
		var ezSmooth;
		if(orientation == \vert) {
			ezSmooth = EZSmoothRangerAntialias(parent, ezSizeVert, label, spec, layout: orientation);
		} {
			ezSmooth = EZSmoothRangerAntialias(parent, ezSizeHorz, label, spec, layout: orientation);
		};
		ezSmooth.rangeSlider
		.knobSize_(knobSize).thumbSize_(thumbSize)
		.hilightColor_(highLightColor)
		.value_(0.5)
		.background_(backgroundSl)
		.align_(\center)
		.value_([0, 0]);
		ezSmooth.loBox
		.font_(font)
		.stringColor_(stringColor)
		.normalColor_(stringColor)
		.align_(\center)
		.background_(backgroundColor);
		ezSmooth.hiBox
		.font_(font)
		.stringColor_(stringColor)
		.normalColor_(stringColor)
		.align_(\center)
		.background_(backgroundColor);
		ezSmooth.labelView
		.align_(\center)
		.font_(font)
		.stringColor_(stringColor)
		.background_(backgroundColor);
		^ezSmooth;
	}

	getSizableEZRanger { |parent, label, bounds, spec, orientation = \vert|
		var ezSmooth;
		if(orientation == \vert) {
			ezSmooth = EZSmoothRangerAntialias(parent, bounds, label, spec, layout: orientation);
		} {
			ezSmooth = EZSmoothRangerAntialias(parent, bounds, label, spec, layout: orientation);
		};
		ezSmooth.rangeSlider
		.knobSize_(knobSize).thumbSize_(thumbSize)
		.hilightColor_(highLightColor)
		.value_(0.5)
		.background_(backgroundSl)
		.align_(\center)
		.value_([0, 0]);
		ezSmooth.loBox
		.font_(font)
		.stringColor_(stringColor)
		.normalColor_(stringColor)
		.align_(\center)
		.background_(backgroundColor);
		ezSmooth.hiBox
		.font_(font)
		.stringColor_(stringColor)
		.normalColor_(stringColor)
		.align_(\center)
		.background_(backgroundColor);
		ezSmooth.labelView
		.align_(\center)
		.font_(font)
		.stringColor_(stringColor)
		.background_(backgroundColor);
		^ezSmooth;
	}

	getPopUpMenu { |parent, width|
		var pop = PopUpMenu(parent, width@gadgetHeight)
		.background_(backgroundColor)
		.stringColor_(stringColor)
		.font_(font);
		^pop
	}


  // as of SC 3.10 you cannot color the checkbox text
	getCheckBox{ |parent, text, boundX=20, boundY=20|
		var check = CheckBox(parent,boundX@boundY,text)
		.background_(backgroundColor)
		.font_(font);
		^check;
	}

  // kludge to allow styling of checkbox text
	getCheckBoxAndLabel {|parent, text, checkWidth=20, checkHeight=20, textWidth=30, textHeight=20|
		var txt, chk;
    chk = CheckBox(parent, checkWidth@checkHeight);
    txt = this.getSizableText(parent, text, textWidth, 'left', 8);
    txt.mouseUpAction_({ chk.valueAction_( chk.value.not ) });
    ^[chk,txt];
	}


  getColoredRect{|parent,color,align=\left|
		^StaticText(parent, 2@20)
		.align_(align)
		.string_( "" )
		.background_(color);
	}

  getHorizontalSpacer {|parent, width|
    ^UserView(parent, width@(gadgetHeight)).drawFunc_({|v|
      var hlrect = v.bounds.insetBy(2);
      //"View Bounds % %".format(hlrect.width, hlrect.height).postln;
      Pen.strokeColor = stringColor;
      Pen.width = 2;
      Pen.moveTo(Point(5, hlrect.height / 2));
      Pen.lineTo(Point(hlrect.width-5, hlrect.height / 2));
      Pen.stroke;
      //Pen.fillAxialGradient(0@0, 0@(hlrect.height), highLightColor, stringColor);
    }).refresh;
  }



	drawLine{|master, bounds, startPoints, endPoints, color|
		UserView(master, bounds)
		.drawFunc = {
			Pen.strokeColor = color;
			Pen.width = 1;
			Pen.moveTo(startPoints);
			Pen.lineTo(endPoints);
			Pen.stroke;
		};
	}

  makeModalConfirmDialog {|title, message, yesLabel="Ok", noLabel="Cancel", yesAction, noAction|
    var win, styler, yesbtn, nobtn, bounds, container;
    bounds = Rect(0,0, master.bounds.width, master.bounds.height);
    win = Window(title, Rect(master.bounds.left, master.bounds.top, master.bounds.width, master.bounds.height), false, false, nil, false);
    win.alpha_(0.8).background_(backgroundColor);
    styler = GUIStyler(win, \black);
    styler.gadgetHeight = 50;
    container = styler.getView("Confirm", bounds, margin: 5@5, gap: 5@5);
    styler.getSizableText(container, message, bounds.width-10, fontSize: 14);
    yesbtn = styler.getSizableButton(container, yesLabel, size: 70@40);
    nobtn = styler.getSizableButton(container, noLabel, size: 70@40);
    yesbtn.action_({|btn|
      if(yesAction.notNil) {yesAction.value};
      win.close;
    });
    nobtn.action_({|btn|
      if(noAction.notNil) {noAction.value};
      win.close;
    });
    win.alwaysOnTop_(true).front;
  }


  makeModalTextEntryDialog {|title, message, textValue="", yesLabel="Ok", noLabel="Cancel", yesAction, noAction|
    var win, styler, textedit, yesbtn, nobtn, bounds, container;
    bounds = Rect(0, 0, master.bounds.width, master.bounds.height);
    win = Window(title, Rect(master.bounds.left, master.bounds.top, master.bounds.width, master.bounds.height), false, true, nil, true);
    win.alpha_(0.8).background_(backgroundColor);
    styler = GUIStyler(win, \black);
    styler.gadgetHeight = 40;
    container = styler.getView("Confirm", bounds, margin: 5@5, gap: 5@5);
    styler.getSizableText(container, message, (bounds.width-10), fontSize: 14);
    textedit = styler.getTextField(container, (bounds.width-10), fontSize: 14).string_(textValue);
    yesbtn = styler.getSizableButton(container, yesLabel, size: 70@40);
    nobtn = styler.getSizableButton(container, noLabel, size: 70@40);
    yesbtn.action_({|btn|
      if(yesAction.notNil) {yesAction.(textedit.string)};
      win.close;
    });
    nobtn.action_({|btn|
      if(noAction.notNil) {noAction.(textedit.string)};
      win.close;
    });
    win.alwaysOnTop_(true).front;
  }



}