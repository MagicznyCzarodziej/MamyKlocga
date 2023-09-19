import Countdown, { CountdownRendererFn } from 'react-countdown';

interface Props {
  endDate: string;
}

export const Timer = (props: Props) => {
  const { endDate } = props;

  return <Countdown date={endDate} renderer={renderer} zeroPadTime={1} onComplete={() => {
    navigator.vibrate(1000)
  }} />;
};

const renderer: CountdownRendererFn = ({ completed, formatted, minutes }) => {
  if (completed) {
    return <div className={`text-center text-3xl text-red-500`}>Koniec czasu!</div>;
  } else {
    return <div className={`text-center mt-6 mb-6 border-b-2 pb-4`}>
      <div className={`text-xl mb-1 text-gray-500`}>Pozostały czas:</div>
      <div className={`text-5xl`}>
        {minutes > 0 && <>
          {formatted.minutes}<span className={`text-3xl`}>min</span>
        </>} {formatted.seconds}<span className={`text-xl`}>s</span>
      </div>
    </div>;
  }
};