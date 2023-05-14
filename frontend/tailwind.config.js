/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx,html}",
    "./src/components/**/*.{js,ts,jsx,tsx,html}",
    "./src/app/**/*.{js,ts,jsx,tsx,html}",
  ],
  theme: {
    extend: {
      colors: {
        body: "rgba(256,256,256,0.5)",
        link: "#3B82F6"
      },
      fontFamily: {
        playfair: ["var(--font-playfair)"],
        roboto: ["var(--font-roboto)"],
      },
    },
  },
  plugins: [],
};
