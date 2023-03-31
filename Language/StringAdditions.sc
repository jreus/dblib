/*_____________________________________________________________

dbLib [additions to SuperCollider]
Copyright (C) <2015>

by Darien Brito
http://www.darienbrito.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

________________________________________________________________*/

// <A hex string to decimal base converter>

+ String {

	// Repeat the given string N times
	repeat { | n |
		^"%".format((this ! n).join);
	}

	// Transform a hex string to decimal base
	hex2decimal {
		var string = this.toUpper;
		var digits = "0123456789ABCDEF";
		var val = 0;
		string = string.replace("#","");
		string.do{|items|
		var place = digits.find(items.asString);
		val = (16*val) + place;
	};
	^val
	}

}


