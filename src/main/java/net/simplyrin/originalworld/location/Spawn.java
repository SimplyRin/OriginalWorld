package net.simplyrin.originalworld.location;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by SimplyRin on 2018/11/11.
 *
 * Copyright (c) 2018 SimplyRin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
@Getter
@AllArgsConstructor
public class Spawn {

	private double x;
	private double y;
	private double z;

	@Override
	public String toString() {
		return this.x + "-" + this.y + "-" + this.z;
	}

	public static Spawn fromString(String args) {
		double x = Double.parseDouble(args.split("-")[0]);
		double y = Double.parseDouble(args.split("-")[1]);
		double z = Double.parseDouble(args.split("-")[2]);

		return new Spawn(x, y, z);
	}

}
