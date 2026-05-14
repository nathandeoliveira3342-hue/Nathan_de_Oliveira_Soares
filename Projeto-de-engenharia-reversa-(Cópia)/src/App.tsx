/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import React, { useState } from 'react';
import MainLayout from './components/layout/MainLayout';
import Accordion from './components/ui/Accordion';
import QRPreview from './components/qr/QRPreview';
import { Github, Upload, RotateCcw } from 'lucide-react';
import { QRCodeOptions, DotsStyle, CornersSquareStyle, CornersDotStyle, Mode, ErrorCorrectionLevel } from './types/qr';

const INITIAL_OPTIONS: QRCodeOptions = {
  data: 'https://qr-code-styling.com',
  width: 300,
  height: 300,
  margin: 0,
  image: 'https://qr-code-styling.com/favicon.ico',
  qrOptions: {
    typeNumber: 0,
    mode: 'Byte',
    errorCorrectionLevel: 'Q'
  },
  dotsOptions: {
    type: 'rounded',
    color: '#6a1a4c'
  },
  cornersSquareOptions: {
    type: 'extra-rounded',
    color: '#000000'
  },
  cornersDotOptions: {
    type: 'dot',
    color: '#000000'
  },
  backgroundOptions: {
    color: '#ffffff'
  },
  imageOptions: {
    hideBackgroundDots: true,
    imageSize: 0.4,
    margin: 0
  }
};

export default function App() {
  const [options, setOptions] = useState<QRCodeOptions>(INITIAL_OPTIONS);

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (event) => {
        setOptions(prev => ({ ...prev, image: event.target?.result as string }));
      };
      reader.readAsDataURL(file);
    }
  };

  return (
    <MainLayout
      header={
        <>
          <div className="flex items-center gap-4">
            <img src="https://qr-code-styling.com/favicon.ico" alt="Logo" className="w-6 h-6" />
            <span className="font-bold text-lg">QR code styling</span>
          </div>
          <div className="flex items-center gap-4 text-sm">
            <a href="#" className="hover:underline">npm v1.8.3</a>
            <a href="https://github.com/kozakdenys/qr-code-styling" target="_blank" className="flex items-center gap-1 hover:underline">
              <Github size={16} /> GitHub
            </a>
          </div>
        </>
      }
      hero={
        <div 
          className="py-16 px-8 border-b border-gray-100 text-white transition-all duration-500"
          style={{ background: `linear-gradient(to right, #000000, ${options.dotsOptions.color}, #ffffff)` }}
        >
          <div className="max-w-7xl mx-auto px-4">
            <h1 className="text-5xl font-bold mb-3 tracking-tight drop-shadow-sm">QR code styling</h1>
            <p className="text-2xl font-light opacity-90 drop-shadow-sm">An open source JS library</p>
            <p className="text-xl font-light opacity-70 drop-shadow-sm">For generating styled QR codes</p>
          </div>
        </div>
      }
      sidebar={
        <div className="bg-white rounded-lg border border-gray-200 overflow-hidden">
          {/* Main Options - Fixed */}
          <div className="p-4 border-b border-gray-100">
            <h3 className="text-sm font-bold uppercase text-gray-800 mb-4">Main Options</h3>
            <div className="grid grid-cols-1 gap-4">
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-gray-400">Data</label>
                <input 
                  type="text" 
                  value={options.data}
                  onChange={(e) => setOptions(prev => ({ ...prev, data: e.target.value }))}
                  className="w-full border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-black"
                />
              </div>
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-gray-400">Image File</label>
                <div className="flex items-center gap-2">
                  <label className="cursor-pointer bg-gray-100 hover:bg-gray-200 px-4 py-2 rounded text-sm flex items-center gap-2 border border-gray-300">
                    <Upload size={14} /> Choose File
                    <input type="file" className="hidden" onChange={handleImageUpload} accept="image/*" />
                  </label>
                  {options.image && (
                    <button 
                      onClick={() => setOptions(prev => ({ ...prev, image: undefined }))}
                      className="text-red-500 hover:text-red-700 text-xs"
                    >
                      Remove
                    </button>
                  )}
                </div>
              </div>
              <div className="grid grid-cols-3 gap-4">
                <div className="space-y-1">
                  <label className="text-xs font-bold uppercase text-gray-400">Width</label>
                  <input 
                    type="number" 
                    value={options.width}
                    onChange={(e) => setOptions(prev => ({ ...prev, width: Number(e.target.value) }))}
                    className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                  />
                </div>
                <div className="space-y-1">
                  <label className="text-xs font-bold uppercase text-gray-400">Height</label>
                  <input 
                    type="number" 
                    value={options.height}
                    onChange={(e) => setOptions(prev => ({ ...prev, height: Number(e.target.value) }))}
                    className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                  />
                </div>
                <div className="space-y-1">
                  <label className="text-xs font-bold uppercase text-gray-400">Margin</label>
                  <input 
                    type="number" 
                    value={options.margin}
                    onChange={(e) => setOptions(prev => ({ ...prev, margin: Number(e.target.value) }))}
                    className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                  />
                </div>
              </div>
            </div>
          </div>

          {/* QR Code Options */}
          <Accordion title="QR Code Options">
            <div className="grid grid-cols-1 gap-4">
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-gray-400">Type Number (0-40)</label>
                <input 
                  type="number" 
                  min="0"
                  max="40"
                  value={options.qrOptions.typeNumber}
                  onChange={(e) => setOptions(prev => ({ ...prev, qrOptions: { ...prev.qrOptions, typeNumber: Number(e.target.value) } }))}
                  className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                />
              </div>
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-gray-400">Mode</label>
                <select 
                  value={options.qrOptions.mode}
                  onChange={(e) => setOptions(prev => ({ ...prev, qrOptions: { ...prev.qrOptions, mode: e.target.value as Mode } }))}
                  className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                >
                  {['Numeric', 'Alphanumeric', 'Byte', 'Kanji'].map(m => (
                    <option key={m} value={m}>{m}</option>
                  ))}
                </select>
              </div>
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-gray-400">Error Correction Level</label>
                <select 
                  value={options.qrOptions.errorCorrectionLevel}
                  onChange={(e) => setOptions(prev => ({ ...prev, qrOptions: { ...prev.qrOptions, errorCorrectionLevel: e.target.value as ErrorCorrectionLevel } }))}
                  className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                >
                  {['L', 'M', 'Q', 'H'].map(l => (
                    <option key={l} value={l}>{l}</option>
                  ))}
                </select>
              </div>
            </div>
          </Accordion>

          {/* Dots Options */}
          <Accordion title="Dots Options">
            <div className="space-y-4">
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-gray-400">Dots Style</label>
                <select 
                  value={options.dotsOptions.type}
                  onChange={(e) => setOptions(prev => ({ ...prev, dotsOptions: { ...prev.dotsOptions, type: e.target.value as DotsStyle } }))}
                  className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                >
                  {['square', 'dots', 'rounded', 'extra-rounded', 'classy', 'classy-rounded'].map(s => (
                    <option key={s} value={s}>{s}</option>
                  ))}
                </select>
              </div>
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-gray-400">Color</label>
                <div className="flex items-center gap-2">
                  <input 
                    type="color" 
                    value={options.dotsOptions.color}
                    onChange={(e) => setOptions(prev => ({ ...prev, dotsOptions: { ...prev.dotsOptions, color: e.target.value } }))}
                    className="w-10 h-10 border-none p-0 cursor-pointer"
                  />
                  <button 
                    onClick={() => setOptions(prev => ({ ...prev, dotsOptions: { ...prev.dotsOptions, color: '#000000' } }))}
                    className="text-xs flex items-center gap-1 text-gray-500 hover:text-black"
                  >
                    <RotateCcw size={12} /> Clear
                  </button>
                </div>
              </div>
            </div>
          </Accordion>

          {/* Corners Square Options */}
          <Accordion title="Corners Square Options">
            <div className="space-y-4">
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-gray-400">Corners Square Style</label>
                <select 
                  value={options.cornersSquareOptions.type}
                  onChange={(e) => setOptions(prev => ({ ...prev, cornersSquareOptions: { ...prev.cornersSquareOptions, type: e.target.value as CornersSquareStyle } }))}
                  className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                >
                  {['square', 'dot', 'extra-rounded'].map(s => (
                    <option key={s} value={s}>{s}</option>
                  ))}
                </select>
              </div>
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-gray-400">Color</label>
                <input 
                  type="color" 
                  value={options.cornersSquareOptions.color}
                  onChange={(e) => setOptions(prev => ({ ...prev, cornersSquareOptions: { ...prev.cornersSquareOptions, color: e.target.value } }))}
                  className="w-10 h-10 border-none p-0 cursor-pointer"
                />
              </div>
            </div>
          </Accordion>

          {/* Corners Dot Options */}
          <Accordion title="Corners Dot Options">
            <div className="space-y-4">
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-gray-400">Corners Dot Style</label>
                <select 
                  value={options.cornersDotOptions.type}
                  onChange={(e) => setOptions(prev => ({ ...prev, cornersDotOptions: { ...prev.cornersDotOptions, type: e.target.value as CornersDotStyle } }))}
                  className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                >
                  {['square', 'dot'].map(s => (
                    <option key={s} value={s}>{s}</option>
                  ))}
                </select>
              </div>
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-gray-400">Color</label>
                <input 
                  type="color" 
                  value={options.cornersDotOptions.color}
                  onChange={(e) => setOptions(prev => ({ ...prev, cornersDotOptions: { ...prev.cornersDotOptions, color: e.target.value } }))}
                  className="w-10 h-10 border-none p-0 cursor-pointer"
                />
              </div>
            </div>
          </Accordion>

          {/* Background Options */}
          <Accordion title="Background Options">
            <div className="space-y-4">
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-gray-400">Background Color</label>
                <input 
                  type="color" 
                  value={options.backgroundOptions.color}
                  onChange={(e) => setOptions(prev => ({ ...prev, backgroundOptions: { ...prev.backgroundOptions, color: e.target.value } }))}
                  className="w-10 h-10 border-none p-0 cursor-pointer"
                />
              </div>
            </div>
          </Accordion>

          {/* Image Options */}
          <Accordion title="Image Options">
            <div className="space-y-4">
              <div className="flex items-center gap-2">
                <input 
                  type="checkbox" 
                  id="hideDots"
                  checked={options.imageOptions.hideBackgroundDots}
                  onChange={(e) => setOptions(prev => ({ ...prev, imageOptions: { ...prev.imageOptions, hideBackgroundDots: e.target.checked } }))}
                  className="w-4 h-4"
                />
                <label htmlFor="hideDots" className="text-sm text-gray-700">Hide Background Dots</label>
              </div>
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-gray-400">Image Size (0.1 - 1.0)</label>
                <input 
                  type="number" 
                  step="0.1"
                  min="0.1"
                  max="1.0"
                  value={options.imageOptions.imageSize}
                  onChange={(e) => setOptions(prev => ({ ...prev, imageOptions: { ...prev.imageOptions, imageSize: Number(e.target.value) } }))}
                  className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                />
              </div>
              <div className="space-y-1">
                <label className="text-xs font-bold uppercase text-gray-400">Image Margin</label>
                <input 
                  type="number" 
                  value={options.imageOptions.margin}
                  onChange={(e) => setOptions(prev => ({ ...prev, imageOptions: { ...prev.imageOptions, margin: Number(e.target.value) } }))}
                  className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                />
              </div>
            </div>
          </Accordion>
        </div>
      }
      preview={<QRPreview options={options} />}
    />
  );
}
