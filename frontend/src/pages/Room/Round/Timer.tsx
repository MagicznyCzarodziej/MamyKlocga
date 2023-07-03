import Countdown, { CountdownRendererFn } from 'react-countdown';

interface Props {
  endDate: string;
}

export const Timer = (props: Props) => {
  const { endDate } = props;

  return <Countdown date={endDate} renderer={renderer} />;
};

const renderer: CountdownRendererFn = ({completed, formatted }) => {
  if (completed) {
    return 'Koniec czasu!';
  } else {
    return <div>Pozostały czas: {formatted.minutes}min {formatted.seconds}s</div>;
  }
};