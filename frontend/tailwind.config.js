/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx,html}",
    "./src/components/**/*.{js,ts,jsx,tsx,html}",
    "./src/app/**/*.{js,ts,jsx,tsx,html}",
  ],
  theme: {
    extend: {
      fontFamily: {
        playfair: [/* "Playfair Display", "serif" */ "var(--font-playfair)"],
        roboto: [/* "Roboto Slab", "serif" */ "var(--font-roboto)"],
      },
    },
  },
  plugins: [],
};
