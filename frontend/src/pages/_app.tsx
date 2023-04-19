import "@/styles/globals.css";
import type { AppProps } from "next/app";
import { Playfair_Display, Roboto_Slab } from "next/font/google";

const robotoslab = Roboto_Slab({
  variable: "--font-roboto",
  weight: ["400", "500", "600", "700"],
  subsets: ["latin"],
});
const playfair = Playfair_Display({
  variable: "--font-playfair",
  weight: ["700"],
  subsets: ["latin"],
});
export default function App({ Component, pageProps }: AppProps) {
  return (
    <main
      className={`${robotoslab.variable} ${playfair.variable} font-roboto min-h-full`}
    >
      <Component {...pageProps} />
    </main>
  );
}
