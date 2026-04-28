import { useEffect, useRef } from 'react';
import QRCodeStyling from 'qr-code-styling';
import { QRCodeOptions } from '../../types/qr';

interface QRPreviewProps {
  options: QRCodeOptions;
}

export default function QRPreview({ options }: QRPreviewProps) {
  const ref = useRef<HTMLDivElement>(null);
  const qrCode = useRef<QRCodeStyling | null>(null);

  useEffect(() => {
    // Inicializa a instância apenas uma vez
    qrCode.current = new QRCodeStyling({
      width: options.width,
      height: options.height,
      data: options.data,
      margin: options.margin,
      image: options.image,
      qrOptions: {
        typeNumber: options.qrOptions.typeNumber as any,
        mode: options.qrOptions.mode as any,
        errorCorrectionLevel: options.qrOptions.errorCorrectionLevel as any
      },
      dotsOptions: {
        type: options.dotsOptions.type as any,
        color: options.dotsOptions.color
      },
      backgroundOptions: {
        color: options.backgroundOptions.color
      },
      imageOptions: {
        crossOrigin: 'anonymous',
        margin: options.imageOptions.margin,
        imageSize: options.imageOptions.imageSize,
        hideBackgroundDots: options.imageOptions.hideBackgroundDots
      },
      cornersSquareOptions: {
        type: options.cornersSquareOptions.type as any,
        color: options.cornersSquareOptions.color
      },
      cornersDotOptions: {
        type: options.cornersDotOptions.type as any,
        color: options.cornersDotOptions.color
      }
    });

    if (ref.current) {
      // Limpa o container antes de anexar
      ref.current.innerHTML = '';
      qrCode.current.append(ref.current);
    }
  }, []);

  useEffect(() => {
    if (qrCode.current) {
      qrCode.current.update({
        data: options.data,
        width: options.width,
        height: options.height,
        margin: options.margin,
        image: options.image,
        qrOptions: {
          typeNumber: options.qrOptions.typeNumber as any,
          mode: options.qrOptions.mode as any,
          errorCorrectionLevel: options.qrOptions.errorCorrectionLevel as any
        },
        dotsOptions: {
          type: options.dotsOptions.type as any,
          color: options.dotsOptions.color
        },
        backgroundOptions: {
          color: options.backgroundOptions.color
        },
        imageOptions: {
          margin: options.imageOptions.margin,
          imageSize: options.imageOptions.imageSize,
          hideBackgroundDots: options.imageOptions.hideBackgroundDots
        },
        cornersSquareOptions: {
          type: options.cornersSquareOptions.type as any,
          color: options.cornersSquareOptions.color
        },
        cornersDotOptions: {
          type: options.cornersDotOptions.type as any,
          color: options.cornersDotOptions.color
        }
      });
    }
  }, [options]);

  return (
    <div className="flex flex-col items-center gap-6 w-full">
      <div className="bg-white p-4 rounded-xl shadow-lg border border-gray-100">
        <div ref={ref} />
      </div>
      
      <div className="flex flex-wrap justify-center gap-2 w-full max-w-sm">
        <button 
          onClick={() => qrCode.current?.download({ name: 'qr-code', extension: 'png' })}
          className="flex-1 bg-[#333333] text-white px-4 py-2 rounded-md hover:bg-black transition-colors font-medium text-sm shadow-sm"
        >
          PNG
        </button>
        <button 
          onClick={() => qrCode.current?.download({ name: 'qr-code', extension: 'jpeg' })}
          className="flex-1 bg-gray-100 text-[#333333] border border-gray-300 px-4 py-2 rounded-md hover:bg-gray-200 transition-colors font-medium text-sm shadow-sm"
        >
          JPG
        </button>
        <button 
          onClick={() => qrCode.current?.download({ name: 'qr-code', extension: 'svg' })}
          className="flex-1 bg-gray-100 text-[#333333] border border-gray-300 px-4 py-2 rounded-md hover:bg-gray-200 transition-colors font-medium text-sm shadow-sm"
        >
          SVG
        </button>
      </div>
    </div>
  );
}
