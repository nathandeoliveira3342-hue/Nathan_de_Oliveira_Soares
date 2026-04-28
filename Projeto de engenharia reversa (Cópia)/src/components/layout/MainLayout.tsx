import React from 'react';

interface MainLayoutProps {
  header: React.ReactNode;
  hero: React.ReactNode;
  sidebar: React.ReactNode;
  preview: React.ReactNode;
}

export default function MainLayout({ header, hero, sidebar, preview }: MainLayoutProps) {
  return (
    <div className="min-h-screen bg-white font-sans text-[#333]">
      {/* Header Section */}
      <header className="bg-[#333333] text-white py-2 px-4 shadow-md">
        <div className="max-w-7xl mx-auto flex justify-between items-center">
          {header}
        </div>
      </header>

      {/* Hero Section (Gradient Band) */}
      <section className="w-full">
        {hero}
      </section>

      {/* Main Content Area */}
      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
          {/* Sidebar (Controls) */}
          <aside className="lg:col-span-7 space-y-4">
            {sidebar}
          </aside>

          {/* Preview (Sticky on Desktop) */}
          <section className="lg:col-span-5">
            <div className="lg:sticky lg:top-8 flex flex-col items-center">
              {preview}
            </div>
          </section>
        </div>
      </main>
    </div>
  );
}
