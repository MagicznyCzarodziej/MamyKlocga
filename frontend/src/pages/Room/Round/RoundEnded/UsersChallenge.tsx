import { Round, RoundUser } from '../../../../api/useGetRoom';
import { useRateChallenge } from '../../../../api/useRateChallenge';
import {
  CheckBoxOutlineBlank,
  CheckBoxOutlined,
} from '@mui/icons-material';
import React from 'react';
import { clsx } from 'clsx';

interface Props {
  roomCode: string;
  round: Round;
}

export const UsersChallenge = (props: Props) => {
  const { round, roomCode } = props;

  const builders = round.users.filter((user) => user.role === 'BUILDER');

  return <div className={`mt-8 border-t-2 pt-8`}>
    <div className={`text-right text-sm text-gray-500 mb-2`}>Zalicz wyzwanie</div>
    {builders.map(user =>
      <UserChallengeRow user={user} roomCode={roomCode} key={user.username} />
    )}
  </div>;
};

interface UserChallengeProps {
  user: RoundUser;
  roomCode: string;
}

export const UserChallengeRow = (props: UserChallengeProps) => {
  const { user, roomCode } = props;

  const rateChallengeMutation = useRateChallenge();

  const rateChallenge = (hasPassed: boolean) => rateChallengeMutation.mutate({
    roomCode: roomCode,
    userId: user.userId,
    hasPassed: hasPassed,
  });

  const ChallengePassed = <div
    className={`p-3 bg-green-400 text-white text-center`}
  >
    <CheckBoxOutlined />
  </div>;
  const ChallengeNotPassed = <div
    className={`p-3 bg-gray-200 text-center`}
  >
    <CheckBoxOutlineBlank />
  </div>;

  return <div
    className={clsx({ 'bg-green-100': user.hasPassedChallenge }, `flex justify-between items-center mb-2`)}
    onClick={() => {
      rateChallenge(!user.hasPassedChallenge);
    }}
  >
    <div className={`text-xl pl-4`}>{user.username}</div>
    {user.hasPassedChallenge ? ChallengePassed : ChallengeNotPassed}
  </div>;
};
