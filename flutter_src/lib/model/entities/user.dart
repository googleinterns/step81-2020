// Adapted from: https://www.youtube.com/watch?v=0HLt1TYA600

import 'package:equatable/equatable.dart';
import 'package:meta/meta.dart';

class User extends Equatable {

  final String userId;
  final String email;
  final String photoUrl;
  final String displayName;

  User({
    @required this.userId,
    @required this.email,
    @required this.photoUrl,
    @required this.displayName,
  });

  @override
  List<Object> get props => [userId, email, photoUrl, displayName];
}
