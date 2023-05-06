import { Logo } from "@/components/Logo";
import { SearchBar } from "@/components/SearchBar";
import "@/styles/globals.css";
import type { AppProps } from "next/app";
import { Playfair_Display, Roboto_Slab } from "next/font/google";
import Link from "next/link";

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
      className={`flex flex-col items-center justify-between py-16 ${robotoslab.variable} ${playfair.variable} font-roboto min-h-screen`}
    >
      <section className="flex flex-col gap-8 w-full items-center">
        <Link href={"/"}>
          <Logo />
        </Link>
        <SearchBar />
      </section>
      <Component {...pageProps} />
      <Link href="/aboutus">
        <h3 className="h3 place-self-center">About Us</h3>
      </Link>
    </main>
  );
}
